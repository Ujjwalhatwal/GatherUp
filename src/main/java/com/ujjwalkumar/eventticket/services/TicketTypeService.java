package com.ujjwalkumar.eventticket.services;

import com.ujjwalkumar.eventticket.domain.entities.Ticket;

import java.util.UUID;

public interface TicketTypeService {
    Ticket purchaseTicket(UUID userId, UUID ticketTypeId);
}
