package com.shopdb.ecocitycraft.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="jwt")
public class JWTConfiguration {
    private long expirationTime;
    private long refreshExpirationTime;
    private String issuer;

    public String getSecret() {
        return secret;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public long getRefreshExpirationTime() {
        return refreshExpirationTime;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public void setRefreshExpirationTime(long refreshExpirationTime) {
        this.refreshExpirationTime = refreshExpirationTime;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}
