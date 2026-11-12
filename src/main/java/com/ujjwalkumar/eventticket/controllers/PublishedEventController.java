package com.ujjwalkumar.eventticket.controllers;

import com.ujjwalkumar.eventticket.domain.entities.Event;
import com.ujjwalkumar.eventticket.dtos.response.ListPublishedEventResponseDto;
import com.ujjwalkumar.eventticket.dtos.response.PublishedEventResponseDto;
import com.ujjwalkumar.eventticket.mappers.EventMapper;
import com.ujjwalkumar.eventticket.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/published-events")
@RequiredArgsConstructor
public class PublishedEventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public ResponseEntity<Page<ListPublishedEventResponseDto>> listOfPublishedEvent(
            @RequestParam(required = false) String q,
            Pageable pageable
    ){

        Page<Event> events;
        if(q != null && ! q.trim().isEmpty()){
            events = eventService.searchPublishedEvent(q,pageable);
        }else{
            events = eventService.listPublishedEvent(pageable);
        }
        Page<ListPublishedEventResponseDto> response = events
                .map(eventMapper::toListPublishedEventResponseDto);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<PublishedEventResponseDto> getPublishedEventDetails(
            @PathVariable UUID eventId
    ){
        Optional<Event> publishedEvent = eventService.getPublishedEvent(eventId);
        Optional<PublishedEventResponseDto> publishedEventResponseDto = publishedEvent.map(eventMapper::toPublishedEventDto);
         return publishedEventResponseDto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

}
