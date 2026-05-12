package com.nck.taxi.b2b.dto;
 

import lombok.Data;

@Data
public class B2BRegisterRequest {
    private String companyName;
    private String email;
    private String password;
    private String phone;
    private String address;
}