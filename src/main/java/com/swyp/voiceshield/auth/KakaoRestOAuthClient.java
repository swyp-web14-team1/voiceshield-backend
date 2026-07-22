package com.swyp.voiceshield.auth;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoRestOAuthClient implements KakaoOAuthClient {

    private final RestClient restClient;
    private final String clientId;
    private final String redirectUri;
    private final String clientSecret;

    public KakaoRestOAuthClient(
            @Value("${kakao.oauth.client-id:}") String clientId,
            @Value("${kakao.oauth.redirect-uri:}") String redirectUri,
            @Value("${kakao.oauth.client-secret:}") String clientSecret
    ) {
        this.restClient = RestClient.create();
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

        return restClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(JsonNode.class);
    }

    private JsonNode requestUser(String accessToken) {
        return restClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(JsonNode.class);
    }

    private String requireText(JsonNode node, String fieldName) {
        JsonNode field = Objects.requireNonNull(node).get(fieldName);
        if (field == null || field.isNull() || field.asText().isBlank()) {
            throw new IllegalStateException("Missing Kakao field: " + fieldName);
        }
        return field.asText();
    }
}
