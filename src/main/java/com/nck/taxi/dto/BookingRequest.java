package com.nck.taxi.dto;

import lombok.Data;

@Data
public class BookingRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String pickupAddress;
    private String dropoffAddress;
    private String bookingDate;   // yyyy-MM-dd
    private String bookingTime;   // HH:mm
    private String tripType;
    private String comment;
}