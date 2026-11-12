package com.ujjwalkumar.eventticket.mappers;

import com.ujjwalkumar.eventticket.domain.entities.Ticket;
import com.ujjwalkumar.eventticket.dtos.response.GetTicketResponseDto;
import com.ujjwalkumar.eventticket.dtos.response.TicketResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

    TicketResponseDto toTicketResponseDto(Ticket ticket);

    @Mapping(target = "price", source = "ticket.ticketType.price")
    @Mapping(target = "description", source = "ticket.ticketType.description")
    @Mapping(target = "eventName", source = "ticket.ticketType.event.name")
    @Mapping(target = "eventVenue", source = "ticket.ticketType.event.venue")
    @Mapping(target = "eventStartTime", source = "ticket.ticketType.event.startTime")
    @Mapping(target = "eventEndTime", source = "ticket.ticketType.event.endTime")
    GetTicketResponseDto toGetTicketResponseDto(Ticket ticket);


}
