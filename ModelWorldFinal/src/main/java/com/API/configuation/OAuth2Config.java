package com.API.configuation;

import org.springframework.context.annotation.Configuration;

import com.google.api.client.util.Value;

@Configuration
public class OAuth2Config {
	@Value("${google.client-id}")
    private String googleClientId;

    @Value("${google.client-secret}")
    private String googleClientSecret;

    @Value("${google.redirect-uri}")
    private String googleRedirectUri;
}
