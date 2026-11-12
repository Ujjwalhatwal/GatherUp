package com.ujjwalkumar.eventticket.controllers;

import com.ujjwalkumar.eventticket.domain.entities.Ticket;
import com.ujjwalkumar.eventticket.dtos.response.GetTicketResponseDto;
import com.ujjwalkumar.eventticket.dtos.response.TicketResponseDto;
import com.ujjwalkumar.eventticket.exceptions.TicketNotFoundException;
import com.ujjwalkumar.eventticket.mappers.TicketMapper;
import com.ujjwalkumar.eventticket.services.QrCodeService;
import com.ujjwalkumar.eventticket.services.TicketService;
import com.ujjwalkumar.eventticket.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final JwtUtil jwtUtil;
    private final TicketMapper ticketMapper;
    private final QrCodeService  qrCodeService;

    @GetMapping
    public ResponseEntity<Page<TicketResponseDto>> listTicketsForUser(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable){
        UUID userId = jwtUtil.parseUserId(jwt);
        Page<Ticket> tickets = ticketService.listTicketsForUser(userId, pageable);
        Page<TicketResponseDto> response = tickets.map(ticketMapper::toTicketResponseDto);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<GetTicketResponseDto> getTicketForUser(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID ticketId){
        UUID userId = jwtUtil.parseUserId(jwt);
        Ticket ticket = ticketService.getTicketForUser(ticketId, userId).orElseThrow(
                () -> new TicketNotFoundException(
                        String.format("Ticket with id %s not found for user with id %s", ticketId, userId)
                )
        );
        GetTicketResponseDto response = ticketMapper.toGetTicketResponseDto(ticket);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{ticketId}/qr-codes")
    public ResponseEntity<?> getQrCodeOfTicket(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID ticketId){
        UUID userId = jwtUtil.parseUserId(jwt);
        byte[] qrCodeImage = qrCodeService.getQrCodeImageForUserAndTicket(userId, ticketId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(qrCodeImage.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(qrCodeImage);
    }
}
