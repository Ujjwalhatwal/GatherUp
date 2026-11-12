package com.ujjwalkumar.eventticket.services.impl;

import com.ujjwalkumar.eventticket.domain.entities.QrCode;
import com.ujjwalkumar.eventticket.domain.entities.Ticket;
import com.ujjwalkumar.eventticket.domain.entities.TicketValidation;
import com.ujjwalkumar.eventticket.domain.enums.QrCodeStatusEnum;
import com.ujjwalkumar.eventticket.domain.enums.TicketValidationMethod;
import com.ujjwalkumar.eventticket.domain.enums.TicketValidationStatusEnum;
import com.ujjwalkumar.eventticket.exceptions.QrCodeNotFoundException;
import com.ujjwalkumar.eventticket.exceptions.TicketNotFoundException;
import com.ujjwalkumar.eventticket.repository.QrCodeRepository;
import com.ujjwalkumar.eventticket.repository.TicketRepository;
import com.ujjwalkumar.eventticket.repository.TicketValidationRepository;
import com.ujjwalkumar.eventticket.services.TicketValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketValidationServiceImpl implements TicketValidationService {

    private final TicketValidationRepository ticketValidationRepository;
    private final QrCodeRepository qrCodeRepository;
    private final TicketRepository ticketRepository;

    @Override
    public TicketValidation validateTicketByQrCode(UUID qrCodeId) {
        QrCode qrCode = qrCodeRepository.findByIdAndStatus(qrCodeId, QrCodeStatusEnum.ACTIVE)
                .orElseThrow(() -> new QrCodeNotFoundException(
                        String.format("QrCode with id %s not found", qrCodeId)
                ));
        Ticket ticket = qrCode.getTicket();

        return validateTicket(ticket);
    }

    @Override
    public TicketValidation validateTicketManually(UUID ticketId) {

        Ticket ticket = ticketRepository.
                findById(ticketId).orElseThrow(TicketNotFoundException::new);

        return validateTicket(ticket);
    }

    private TicketValidation validateTicket(Ticket ticket) {
        TicketValidation ticketValidation = new TicketValidation();
        ticketValidation.setTicket(ticket);
        ticketValidation.setValidationMethod(TicketValidationMethod.QR_SCAN);

        TicketValidationStatusEnum ticketValidationStatus = ticket.getTicketValidations().stream()
                .filter(tv -> TicketValidationStatusEnum.VALID.equals(tv.getStatus()))
                .findFirst()
                .map(v -> TicketValidationStatusEnum.INVALID)
                .orElse(TicketValidationStatusEnum.VALID);

        ticketValidation.setStatus(ticketValidationStatus);
        return ticketValidationRepository.save(ticketValidation);
    }

}
