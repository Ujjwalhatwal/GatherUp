package com.ujjwalkumar.eventticket.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublishedTicketTypeResponseDto {
    private UUID id;
    private String name;
    private Double price;
    private String description;
}
