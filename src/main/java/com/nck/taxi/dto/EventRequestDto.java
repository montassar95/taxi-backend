package com.nck.taxi.dto;

import lombok.Data;

@Data
public class EventRequestDto {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String eventType;
    private String eventDate;
    private String startTime;
    private Integer numberOfPersons;
    private Integer numberOfTaxis;
    private String tripType;
    private String comment;
}