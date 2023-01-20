package co.inajar.oursponsors.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 1L;

    public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        setAuthenticated(true);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Object getCredentials() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getPrincipal() {
        // TODO Auto-generated method stub
        return null;
    }

}
