package com.ujjwalkumar.eventticket.dtos.response;

import com.ujjwalkumar.eventticket.domain.enums.TicketValidationMethod;
import com.ujjwalkumar.eventticket.domain.enums.TicketValidationStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketValidationResponseDto {

    private UUID ticketId;
    private TicketValidationStatusEnum status;
}
