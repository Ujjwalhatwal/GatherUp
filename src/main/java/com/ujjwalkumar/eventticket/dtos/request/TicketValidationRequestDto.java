package com.ujjwalkumar.eventticket.dtos.request;

import com.ujjwalkumar.eventticket.domain.enums.TicketValidationMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketValidationRequestDto {
    private UUID id;
    private TicketValidationMethod validationMethod;
}
