package com.swyp.voiceshield.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
public class KakaoRestOAuthClient implements KakaoOAuthClient {

    private static final Logger log = LoggerFactory.getLogger(KakaoRestOAuthClient.class);

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String clientId;
    private final String redirectUri;
    private final String clientSecret;

    public KakaoRestOAuthClient(
            ObjectMapper objectMapper,
            @Value("${kakao.oauth.client-id:}") String clientId,
            @Value("${kakao.oauth.redirect-uri:}") String redirectUri,
            @Value("${kakao.oauth.client-secret:}") String clientSecret
    ) {
        this.restClient = RestClient.create();
        this.objectMapper = objectMapper;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.clientSecret = clientSecret;
    }

    @Override
    public KakaoUserProfile retrieveUserProfile(String authorizationCode) {
        JsonNode tokenResponse = requestToken(authorizationCode);
        String accessToken = requireText(tokenResponse, "access_token");
        JsonNode userResponse = requestUser(accessToken);
        return new KakaoUserProfile(requireText(userResponse, "id"));
    }

    private JsonNode requestToken(String authorizationCode) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", clientId);
        form.add("redirect_uri", redirectUri);
        form.add("code", authorizationCode);
        if (clientSecret != null && !clientSecret.isBlank()) {
            form.add("client_secret", clientSecret);
        }

        try {
            ResponseEntity<String> response = restClient.post()
                    .uri("https://kauth.kakao.com/oauth/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .toEntity(String.class);
            return readJson(
                    response.getBody(),
                    "token",
                    response.getHeaders().getContentType() != null
                            ? response.getHeaders().getContentType().toString()
                            : "unknown"
            );
        } catch (RestClientResponseException exception) {
            log.warn(
                    "Kakao token exchange failed. status={}, responseBody={}, redirectUri={}, clientIdSet={}, clientSecretSet={}",
                    exception.getStatusCode(),
                    exception.getResponseBodyAsString(),
                    redirectUri,
                    isPresent(clientId),
                    isPresent(clientSecret)
            );
            throw exception;
        }
    }

    private JsonNode requestUser(String accessToken) {
        try {
            ResponseEntity<String> response = restClient.get()
                    .uri("https://kapi.kakao.com/v2/user/me")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .toEntity(String.class);
            return readJson(
                    response.getBody(),
                    "user",
                    response.getHeaders().getContentType() != null
                            ? response.getHeaders().getContentType().toString()
                            : "unknown"
            );
        } catch (RestClientResponseException exception) {
            log.warn(
                    "Kakao user profile request failed. status={}, responseBody={}",
                    exception.getStatusCode(),
                    exception.getResponseBodyAsString()
            );
            throw exception;
        }
    }

    private String requireText(JsonNode node, String fieldName) {
        JsonNode field = Objects.requireNonNull(node).get(fieldName);
        if (field == null || field.isNull() || field.asText().isBlank()) {
            throw new IllegalStateException("Missing Kakao field: " + fieldName);
        }
        return field.asText();
    }

    private boolean isPresent(String value) {
        return value != null && !value.isBlank();
    }

    private JsonNode readJson(String rawBody, String phase, String contentType) {
        try {
            return objectMapper.readTree(rawBody);
        } catch (JsonProcessingException exception) {
            log.warn(
                    "Kakao {} response parsing failed. contentType={}, rawBody={}",
                    phase,
                    contentType,
                    rawBody
            );
            throw new IllegalStateException("Failed to parse Kakao " + phase + " response", exception);
        }
    }
}
