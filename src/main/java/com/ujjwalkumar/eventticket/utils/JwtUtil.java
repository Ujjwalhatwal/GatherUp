package com.ujjwalkumar.eventticket.utils;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
public class JwtUtil {

    public UUID parseUserId(Jwt jwt){
        return UUID.fromString(Objects.requireNonNull(jwt.getSubject()));
    }
}
