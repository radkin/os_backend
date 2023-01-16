package co.inajar.oursponsors.configuration;

public class JwtConfiguration {
    private String secret;
    private long expiresSeconds;

    public String getSecret() {
        return secret;
    }

    public void setSecret(final String secret) {
        this.secret = secret;
    }

    public long getExpiresSeconds() {
        return expiresSeconds;
    }

    public void setExpiresSeconds(final long expiresSeconds) {
        this.expiresSeconds = expiresSeconds;
    }
}
