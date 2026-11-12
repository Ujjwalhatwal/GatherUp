package com.ujjwalkumar.eventticket.domain;

import com.ujjwalkumar.eventticket.domain.enums.EventStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventRequest {

    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String venue;
    private LocalDateTime salesStart;
    private LocalDateTime salesEnd;
    private EventStatusEnum status;

    private List<CreateTicketTypeRequest> ticketTypes = new ArrayList<>();

}
