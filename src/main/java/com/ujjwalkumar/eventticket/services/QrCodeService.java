package com.ujjwalkumar.eventticket.services;

import com.ujjwalkumar.eventticket.domain.entities.QrCode;
import com.ujjwalkumar.eventticket.domain.entities.Ticket;

import java.util.UUID;

public interface QrCodeService {

    QrCode generateQrCodeForTicket(Ticket ticket);

    byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId);

}
