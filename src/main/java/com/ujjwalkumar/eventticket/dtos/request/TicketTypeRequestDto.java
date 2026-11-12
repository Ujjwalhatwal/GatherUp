package com.ujjwalkumar.eventticket.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketTypeRequestDto {
    @NotBlank(message = "Ticket type is required.")
    private String name;

    @NotNull(message = "Ticket price is required.")
    @PositiveOrZero(message = "Ticket price must zero or greater.")
    private Double price;

    private Integer totalAvailable;

    private String description;

}
