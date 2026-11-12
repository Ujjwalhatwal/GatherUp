package com.ujjwalkumar.eventticket.controllers;

import com.ujjwalkumar.eventticket.domain.CreateEventRequest;
import com.ujjwalkumar.eventticket.domain.UpdateEventRequest;
import com.ujjwalkumar.eventticket.domain.entities.Event;
import com.ujjwalkumar.eventticket.dtos.request.EventRequestDto;
import com.ujjwalkumar.eventticket.dtos.request.UpdateEventRequestDto;
import com.ujjwalkumar.eventticket.dtos.response.EventResponseDto;
import com.ujjwalkumar.eventticket.mappers.EventMapper;
import com.ujjwalkumar.eventticket.services.EventService;
import com.ujjwalkumar.eventticket.utils.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<EventResponseDto> createEvent(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody EventRequestDto eventRequestDto
            ){
        CreateEventRequest createEventRequest = eventMapper.fromDto(eventRequestDto);
        UUID userId = jwtUtil.parseUserId(jwt);
        Event createdEvent= eventService.createEvent(userId, createEventRequest);
        EventResponseDto response =  eventMapper.toDto(createdEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<EventResponseDto>> listEvents(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable
    ){
        UUID userId =  jwtUtil.parseUserId(jwt);
        Page<Event> events = eventService.listEventsByOrganizer( userId, pageable);
        Page<EventResponseDto> response = events.map(eventMapper::toDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> getEventForEventIdAndOrganizer(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable UUID eventId
    ){
        UUID userId = jwtUtil.parseUserId(jwt);
        Optional<Event> event = eventService.getEventByIdAndOrganizer(eventId, userId);
        if(event.isPresent()){
            EventResponseDto response = eventMapper.toDto(event.get());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> updateEvent(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID eventId,
            @Valid @RequestBody UpdateEventRequestDto updateEventRequestDto
    ){
        UpdateEventRequest updateEventRequest = eventMapper.fromDto(updateEventRequestDto);
        UUID userId = jwtUtil.parseUserId(jwt);
        Event updatedEvent = eventService.updateEventForOrganizer(eventId,userId, updateEventRequest);
        EventResponseDto response =  eventMapper.toDto(updatedEvent);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID eventId
    ){
        UUID userId = jwtUtil.parseUserId(jwt);
        eventService.deleteEvent(eventId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
