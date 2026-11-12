package com.ujjwalkumar.eventticket.dtos.response;

import com.ujjwalkumar.eventticket.domain.enums.TicketStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponseDto {

    private UUID id;
    private TicketStatusEnum status;
    private PublishedTicketTypeResponseDto ticketType;
}
