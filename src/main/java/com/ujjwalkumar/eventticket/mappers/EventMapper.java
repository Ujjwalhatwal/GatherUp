package com.ujjwalkumar.eventticket.mappers;

import com.ujjwalkumar.eventticket.domain.CreateEventRequest;
import com.ujjwalkumar.eventticket.domain.CreateTicketTypeRequest;
import com.ujjwalkumar.eventticket.domain.UpdateEventRequest;
import com.ujjwalkumar.eventticket.domain.UpdateTicketTypeRequest;
import com.ujjwalkumar.eventticket.domain.entities.Event;
import com.ujjwalkumar.eventticket.domain.entities.TicketType;
import com.ujjwalkumar.eventticket.dtos.request.EventRequestDto;
import com.ujjwalkumar.eventticket.dtos.request.TicketTypeRequestDto;
import com.ujjwalkumar.eventticket.dtos.request.UpdateEventRequestDto;
import com.ujjwalkumar.eventticket.dtos.request.UpdateTicketTypeRequestDto;
import com.ujjwalkumar.eventticket.dtos.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    CreateTicketTypeRequest fromDto(TicketTypeRequestDto dto);

    CreateEventRequest fromDto(EventRequestDto dto);

    EventResponseDto toDto(Event request);

    TicketTypeResponseDto toDto(TicketType request);

    UpdateEventRequest fromDto(UpdateEventRequestDto dto);

    UpdateTicketTypeRequest fromDto(UpdateTicketTypeRequestDto dto);

    ListPublishedEventResponseDto toListPublishedEventResponseDto(Event request);

    PublishedEventResponseDto toPublishedEventDto(Event request);

    PublishedTicketTypeResponseDto toPublishedTicketTypeDto(TicketType request);

}