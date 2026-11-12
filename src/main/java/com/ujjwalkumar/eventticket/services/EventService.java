package com.ujjwalkumar.eventticket.services;

import com.ujjwalkumar.eventticket.domain.CreateEventRequest;
import com.ujjwalkumar.eventticket.domain.UpdateEventRequest;
import com.ujjwalkumar.eventticket.domain.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest event);

    Page<Event> listEventsByOrganizer(UUID organizerId, Pageable pageable);

    Optional<Event> getEventByIdAndOrganizer(UUID eventId, UUID organizerId);

    Event updateEventForOrganizer(UUID eventId, UUID organizerId, UpdateEventRequest event);

    void deleteEvent(UUID eventId, UUID organizerId);

    Page<Event> listPublishedEvent(Pageable pageable);

    Page<Event> searchPublishedEvent(String query, Pageable pageable);

    Optional<Event> getPublishedEvent(UUID eventId);
}
