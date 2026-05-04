package com.nck.taxi.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String driverId;
    private String name;
    private String role;  // ← nouveau
}