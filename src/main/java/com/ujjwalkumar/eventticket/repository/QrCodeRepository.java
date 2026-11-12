package com.ujjwalkumar.eventticket.repository;

import com.ujjwalkumar.eventticket.domain.entities.QrCode;
import com.ujjwalkumar.eventticket.domain.enums.QrCodeStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, UUID> {

    Optional<QrCode> findByTicketIdAndTicketPurchaserId(UUID ticketId, UUID ticketPurchaseId);
    Optional<QrCode> findByIdAndStatus(UUID qrCodeId, QrCodeStatusEnum status);
}
