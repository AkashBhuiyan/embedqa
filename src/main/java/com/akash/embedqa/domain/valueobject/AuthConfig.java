package com.akash.embedqa.domain.valueobject;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Base64;

/**
 * Author: akash
 * Date: 23/12/25
 */
@Getter
@EqualsAndHashCode
@ToString(exclude = {
        "bearerToken",
        "basicPassword",
        "apiKey",
        "oauth2AccessToken"
})
@Builder
public final class AuthConfig {

    private final String bearerToken;
    private final String basicUsername;
    private final String basicPassword;
    private final String apiKey;

    @Builder.Default
    private final String apiKeyHeaderName = "X-API-Key";

    @Builder.Default
    private final String apiKeyLocation = "header";

    private final String oauth2AccessToken;

    public static AuthConfig bearerToken(String token) {
        return AuthConfig.builder()
                .bearerToken(token)
                .build();
    }

    public static AuthConfig basicAuth(String username, String password) {
        return AuthConfig.builder()
                .basicUsername(username)
                .basicPassword(password)
                .build();
    }

    public String getBasicAuthHeader() {
        if (basicUsername != null && basicPassword != null) {
            String credentials = basicUsername + ":" + basicPassword;
            return "Basic " + Base64.getEncoder()
                    .encodeToString(credentials.getBytes());
        }
        return null;
    }
}
