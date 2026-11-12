package com.ujjwalkumar.eventticket.controllers;

import com.ujjwalkumar.eventticket.services.TicketTypeService;
import com.ujjwalkumar.eventticket.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events/{eventId}/ticket-types")
@RequiredArgsConstructor
public class TicketTypeController {

    private final TicketTypeService ticketTypeService;
    private final JwtUtil jwtUtil;

    @PostMapping("/{ticketTypeId}/purchase")
    public ResponseEntity<Void> purchaseTicket(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID ticketTypeId
    ){
        UUID userId = jwtUtil.parseUserId(jwt);
        ticketTypeService.purchaseTicket(userId, ticketTypeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
