package com.ujjwalkumar.eventticket.services.impl;

import com.ujjwalkumar.eventticket.domain.CreateEventRequest;
import com.ujjwalkumar.eventticket.domain.UpdateEventRequest;
import com.ujjwalkumar.eventticket.domain.UpdateTicketTypeRequest;
import com.ujjwalkumar.eventticket.domain.entities.Event;
import com.ujjwalkumar.eventticket.domain.entities.TicketType;
import com.ujjwalkumar.eventticket.domain.entities.User;
import com.ujjwalkumar.eventticket.domain.enums.EventStatusEnum;
import com.ujjwalkumar.eventticket.exceptions.EventNotFoundException;
import com.ujjwalkumar.eventticket.exceptions.EventUpdateException;
import com.ujjwalkumar.eventticket.exceptions.TicketTypeNotFoundException;
import com.ujjwalkumar.eventticket.exceptions.UserNotFoundException;
import com.ujjwalkumar.eventticket.repository.EventRepository;
import com.ujjwalkumar.eventticket.repository.UserRepository;
import com.ujjwalkumar.eventticket.services.EventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public Event createEvent(UUID organizerId, CreateEventRequest event) {
        User organizer =  userRepository.findById(organizerId).orElseThrow(
                () -> new UserNotFoundException(
                        String.format("User with id '%s' not found", organizerId))
                );

        Event eventToCreate = new Event();

        List<TicketType>  ticketTypeToCreateList = event.getTicketTypes().stream().map(
                ticketType -> {
                    TicketType ticketTypeToCreate = new TicketType();
                    ticketTypeToCreate.setName(ticketType.getName());
                    ticketTypeToCreate.setPrice(ticketType.getPrice());
                    ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
                    ticketTypeToCreate.setDescription(ticketType.getDescription());
                    ticketTypeToCreate.setEvent(eventToCreate);
                    return ticketTypeToCreate;
                }).toList();

        eventToCreate.setName(event.getName());
        eventToCreate.setStartTime(event.getStartTime());
        eventToCreate.setEndTime(event.getEndTime());
        eventToCreate.setVenue(event.getVenue());
        eventToCreate.setSalesStart(event.getSalesStart());
        eventToCreate.setSalesEnd(event.getSalesEnd());
        eventToCreate.setStatus(event.getStatus());
        eventToCreate.setOrganizer(organizer);

        eventToCreate.setTicketTypes(ticketTypeToCreateList);

        return eventRepository.save(eventToCreate);
    }

    @Override
    public Page<Event> listEventsByOrganizer(UUID organizerId, Pageable pageable) {
         return eventRepository.findByOrganizerId(organizerId, pageable);
    }

    @Override
    public Optional<Event> getEventByIdAndOrganizer(UUID eventId, UUID organizerId) {
        return eventRepository.findByIdAndOrganizerId(eventId, organizerId);
    }

    @Override
    @Transactional
    public Event updateEventForOrganizer(UUID eventId, UUID organizerId, UpdateEventRequest event) {
        if(event.getId() == null){
            throw new EventUpdateException("Event Id can't be null");
        }
        if(!eventId.equals(event.getId())){
            throw new EventUpdateException("Cannot update the id of an event");
        }

        Event existingEvent = eventRepository.findByIdAndOrganizerId(eventId, organizerId)
                .orElseThrow(() -> new EventNotFoundException(
                        String.format("Event with Id '%s' does not exists.", eventId))
                );

        existingEvent.setName(event.getName());
        existingEvent.setStartTime(event.getStartTime());
        existingEvent.setEndTime(event.getEndTime());
        existingEvent.setVenue(event.getVenue());
        existingEvent.setSalesStart(event.getSalesStart());
        existingEvent.setSalesEnd(event.getSalesEnd());
        existingEvent.setStatus(event.getStatus());

        Set<UUID> requestTicketTypeIds = event.getTicketTypes()
                .stream().map(UpdateTicketTypeRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingEvent.getTicketTypes().removeIf(existingTicketType ->
                !requestTicketTypeIds.contains(existingTicketType.getId())
        );

        Map<UUID, TicketType> existingTicketTypeIndex = existingEvent.getTicketTypes().stream()
                .collect(Collectors.toMap(TicketType::getId, Function.identity()));

        for(UpdateTicketTypeRequest ticketType : event.getTicketTypes()){
            if(ticketType.getId() == null){
                // Create
                TicketType ticketTypeToCreate = new TicketType();
                ticketTypeToCreate.setName(ticketType.getName());
                ticketTypeToCreate.setPrice(ticketType.getPrice());
                ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
                ticketTypeToCreate.setDescription(ticketType.getDescription());
                ticketTypeToCreate.setEvent(existingEvent);
                existingEvent.getTicketTypes().add(ticketTypeToCreate);

            }else if(existingTicketTypeIndex.containsKey(ticketType.getId())){
                // update case
                TicketType existingTicketType = existingTicketTypeIndex.get(ticketType.getId());
                existingTicketType.setName(ticketType.getName());
                existingTicketType.setPrice(ticketType.getPrice());
                existingTicketType.setTotalAvailable(ticketType.getTotalAvailable());
                existingTicketType.setDescription(ticketType.getDescription());

            }else{
                throw new TicketTypeNotFoundException(
                        String.format("Ticket Type with id: '%s' does not exist",ticketType.getId())
                );
            }
        }

        return eventRepository.save(existingEvent);
    }

    @Override
    @Transactional
    public void deleteEvent(UUID eventId, UUID organizerId) {
        getEventByIdAndOrganizer(eventId,organizerId).ifPresent(eventRepository::delete);
    }

    @Override
    public Page<Event> listPublishedEvent(Pageable pageable) {
        return eventRepository.findByStatus(EventStatusEnum.PUBLISHED,pageable);
    }

    @Override
    public Page<Event> searchPublishedEvent(String query, Pageable pageable) {
        return eventRepository.searchEvents(query,pageable);
    }

    @Override
    public Optional<Event> getPublishedEvent(UUID eventId) {
       return eventRepository.findByIdAndStatus(eventId,EventStatusEnum.PUBLISHED);
    }

}
