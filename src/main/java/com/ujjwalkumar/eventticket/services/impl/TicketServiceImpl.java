package com.ujjwalkumar.eventticket.services.impl;

import com.ujjwalkumar.eventticket.domain.entities.Ticket;
import com.ujjwalkumar.eventticket.repository.TicketRepository;
import com.ujjwalkumar.eventticket.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    @Override
    public Page<Ticket> listTicketsForUser(UUID userId, Pageable pageable) {
        return ticketRepository.findByPurchaserId(userId, pageable);
    }

    @Override
    public Optional<Ticket> getTicketForUser(UUID ticketId, UUID userId) {
        return ticketRepository.findByIdAndPurchaserId(ticketId, userId);
    }
}
