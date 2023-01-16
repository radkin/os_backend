package co.inajar.oursponsors.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "oursponsors")
public class ApplicationConfigurationProperties {
    private JwtConfiguration jwt;
    private HealthCheckManagerConfiguration healthCheckManager;

    public JwtConfiguration getJwt() {
        return jwt;
    }

    public void setJwt(final JwtConfiguration jwt) {
        this.jwt = jwt;
    }

    public HealthCheckManagerConfiguration getHealthCheckManager() {
        return healthCheckManager;
    }

    public void setHealthCheckManager(HealthCheckManagerConfiguration healthCheckManager) {
        this.healthCheckManager = healthCheckManager;
    }
}
