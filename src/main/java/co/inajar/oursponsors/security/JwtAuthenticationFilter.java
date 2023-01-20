package co.inajar.oursponsors.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;

import co.inajar.oursponsors.configuration.ApplicationConfigurationProperties;
import co.inajar.oursponsors.services.UserManager;
import co.inajar.oursponsors.dbOs.entities.User;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static co.inajar.oursponsors.security.SecurityConstants.*;
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private UserManager userManager;

    private final ApplicationConfigurationProperties configProps;

    public JwtAuthenticationFilter(AuthenticationManager authManager, ApplicationConfigurationProperties configProps, UserManager userManager) {
        super(authManager);
        this.configProps = configProps;
        this.userManager = userManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        var header = req.getHeader(HEADER_STRING);
        var inajarToken = req.getHeader(INAJAR_TOKEN) != null ? req.getHeader(INAJAR_TOKEN) : req.getParameter(TOKEN) ;

        if ((header == null || !header.startsWith(TOKEN_PREFIX)) && inajarToken == null) {
            SecurityContextHolder.clearContext();
        } else {
            var authentication = getAuthentication(req);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(req, res);

    }

    private JwtAuthenticationToken getAuthentication(HttpServletRequest request) {

        var token = request.getHeader(HEADER_STRING);
        var inajarToken = request.getHeader(INAJAR_TOKEN) != null ? request.getHeader(INAJAR_TOKEN) : request.getParameter(TOKEN) ;
        if(inajarToken != null) {
            final Optional<User> optionalUser = userManager.getUserByApiKey(inajarToken);
            if(optionalUser.isPresent()) {
                var auth = new JwtAuthenticationToken(null);
                auth.setDetails(optionalUser.get());
                return auth;
            }
        }
        if (token != null) {
            System.out.println("SECRET!!!!!");
            System.out.println(configProps.getJwt().getSecret());
            System.out.println(token);

            // parse the token.
            final var jwt = JWT.require(Algorithm.HMAC256(configProps.getJwt().getSecret())).build()
                    .verify(token.replace(TOKEN_PREFIX, ""));

            // check roles in token (we will probably not provide roles via JWT but we can change this to get roles by useId or inajar_api_token )

            final Claim roles = jwt.getClaim(ROLES);
            List<JwtGrantedAuthority> authorities = null;
            if(! roles.isNull())
                authorities = roles.asList(String.class).stream().map((sRole) -> {
                    final var role = InAJarAccessRole.valueOf(sRole);
                    return new JwtGrantedAuthority(role);
                }).collect(Collectors.toUnmodifiableList());

            final Claim id = jwt.getClaim(ID);
            final Claim apiKey = jwt.getClaim(INAJAR_API_KEY);

            if(id.isNull() && apiKey.isNull() ) {
                System.out.println("BOTH NULL!!!!");
                return null;
            }
            final Optional<User> optionalUser = id.isNull() ? userManager.getUserByApiKey(apiKey.asString()) : userManager.getUserById(id.asLong());
            if(optionalUser.isPresent()) {
                var auth = new JwtAuthenticationToken(authorities);
                auth.setDetails(optionalUser.get());
                return auth;
            }

        }
        return null;
    }

}
