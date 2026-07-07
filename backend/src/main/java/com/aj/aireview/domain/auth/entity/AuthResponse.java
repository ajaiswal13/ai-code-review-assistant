package com.aj.aireview.domain.auth.entity;

public record AuthResponse(
        String accessToken,

        String tokenType
) {
}
