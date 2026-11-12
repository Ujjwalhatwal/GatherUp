package com.ujjwalkumar.eventticket.controllers;

import com.ujjwalkumar.eventticket.domain.entities.TicketValidation;
import com.ujjwalkumar.eventticket.domain.enums.TicketValidationMethod;
import com.ujjwalkumar.eventticket.dtos.request.TicketValidationRequestDto;
import com.ujjwalkumar.eventticket.dtos.response.TicketResponseDto;
import com.ujjwalkumar.eventticket.dtos.response.TicketValidationResponseDto;
import com.ujjwalkumar.eventticket.mappers.TicketValidationMapper;
import com.ujjwalkumar.eventticket.services.TicketValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ticket-validation")
@RequiredArgsConstructor
public class TicketValidationController {

    private final TicketValidationService ticketValidationService;
    private final TicketValidationMapper ticketValidationMapper;

    @PostMapping
    public ResponseEntity<TicketValidationResponseDto> validateTicket(
            @RequestBody TicketValidationRequestDto ticketValidationRequestDto
            ) {
        // Implement the ticket validation logic here
        TicketValidationMethod validationMethod = ticketValidationRequestDto.getValidationMethod();

        TicketValidation ticketValidation;
        if(TicketValidationMethod.MANUAL.equals(validationMethod)){
            ticketValidation = ticketValidationService.validateTicketManually(ticketValidationRequestDto.getId());
        }else {
            ticketValidation = ticketValidationService.validateTicketByQrCode(ticketValidationRequestDto.getId());
        }

        TicketValidationResponseDto reponse = ticketValidationMapper.toValidateDto(ticketValidation);
        return ResponseEntity.ok().body(reponse);
    }
}
