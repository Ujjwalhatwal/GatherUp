package com.ujjwalkumar.eventticket.services.impl;

import com.ujjwalkumar.eventticket.domain.entities.Ticket;
import com.ujjwalkumar.eventticket.domain.entities.TicketType;
import com.ujjwalkumar.eventticket.domain.entities.User;
import com.ujjwalkumar.eventticket.domain.enums.TicketStatusEnum;
import com.ujjwalkumar.eventticket.exceptions.TicketSoldOutException;
import com.ujjwalkumar.eventticket.exceptions.TicketTypeNotFoundException;
import com.ujjwalkumar.eventticket.exceptions.UserNotFoundException;
import com.ujjwalkumar.eventticket.repository.TicketRepository;
import com.ujjwalkumar.eventticket.repository.TicketTypeRepository;
import com.ujjwalkumar.eventticket.repository.UserRepository;
import com.ujjwalkumar.eventticket.services.QrCodeService;
import com.ujjwalkumar.eventticket.services.TicketTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final QrCodeService qrCodeService;

    @Override
    @Transactional
    public Ticket purchaseTicket(UUID userId, UUID ticketTypeId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("User with ID %s not found", userId))
        );

        /**
         *  findByIdWithLock is used to lock the ticket type row in the database to prevent race conditions
         *  when multiple users are trying to purchase tickets for the same ticket type at the same time.
         *  This ensures that the available quantity of tickets is accurately checked and updated,
         *  preventing overselling of tickets.
         */
        TicketType ticketType = ticketTypeRepository.findByIdWithLock(ticketTypeId).orElseThrow(
                () -> new TicketTypeNotFoundException(String.format("Ticket type with ID %s not found", ticketTypeId))
        );

        int purchasedTickets = ticketRepository.countByTicketTypeId(ticketType.getId());
        Integer totalAvailable = ticketType.getTotalAvailable();

        if(purchasedTickets + 1 > totalAvailable){
             throw new TicketSoldOutException(String.format("Ticket type with ID %s is sold out", ticketTypeId));
        }

        Ticket ticket = new Ticket();
        ticket.setStatus(TicketStatusEnum.PURCHASED);
        ticket.setTicketType(ticketType);
        ticket.setPurchaser(user);

        Ticket savedTicket = ticketRepository.save(ticket);
        qrCodeService.generateQrCodeForTicket(savedTicket);

        return ticketRepository.save(savedTicket);
    }
}
