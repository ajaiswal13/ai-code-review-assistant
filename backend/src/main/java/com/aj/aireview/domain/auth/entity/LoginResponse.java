package com.aj.aireview.domain.auth.entity;

public record LoginResponse(
        String accessToken,
        String tokenType
) {
}
