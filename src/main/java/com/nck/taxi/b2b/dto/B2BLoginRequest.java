package com.nck.taxi.b2b.dto;
 

import lombok.Data;

@Data
public class B2BLoginRequest {
    private String email;
    private String password;
}