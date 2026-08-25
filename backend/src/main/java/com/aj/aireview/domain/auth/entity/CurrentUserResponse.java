package com.aj.aireview.domain.auth.entity;

public record CurrentUserResponse(
        String email,
        String firstName,
        String lastName,
        String role
) {
}
