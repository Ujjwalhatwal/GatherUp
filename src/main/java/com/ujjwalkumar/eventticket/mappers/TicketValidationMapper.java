package com.ujjwalkumar.eventticket.mappers;


import com.ujjwalkumar.eventticket.domain.entities.TicketValidation;
import com.ujjwalkumar.eventticket.dtos.request.TicketValidationRequestDto;
import com.ujjwalkumar.eventticket.dtos.response.TicketResponseDto;
import com.ujjwalkumar.eventticket.dtos.response.TicketValidationResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketValidationMapper {

    @Mapping(target = "ticketId", source = "ticket.id")
    TicketValidationResponseDto toValidateDto(TicketValidation request);

}
