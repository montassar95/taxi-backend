package com.nck.taxi.dto;

import lombok.Data;

@Data
public class B2BRequestDto {
    private String companyName;
    private String contactName;
    private String phone;
    private String email;
    private String address;
    private String city;
    private String serviceType;
    private String dailyRides;
    private String frequency;
    private String schedule;
    private String usersCount;
    private String comment;
}