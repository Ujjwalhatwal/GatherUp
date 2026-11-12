package com.ujjwalkumar.eventticket.dtos.request;

import com.ujjwalkumar.eventticket.domain.enums.EventStatusEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestDto {

    @NotBlank(message = "Event name is required.")
    private String name;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @NotBlank(message = "Venue information is required.")
    private String venue;
    private LocalDateTime salesStart;
    private LocalDateTime salesEnd;

    @NotNull(message = "Event status must be provided.")
    private EventStatusEnum status;

    @NotEmpty(message = "At least one ticket type must be provided.")
    @Valid
    private List<TicketTypeRequestDto> ticketTypes;
}
