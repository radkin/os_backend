package co.inajar.oursponsors.security;

import org.springframework.security.core.GrantedAuthority;

public class JwtGrantedAuthority implements GrantedAuthority {

    private static final long serialVersionUID = 1L;
    private final InAJarAccessRole role;

    public JwtGrantedAuthority(final InAJarAccessRole role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.name();
    }

}
