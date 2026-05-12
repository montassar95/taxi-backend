package com.nck.taxi.b2b.dto;

import lombok.Data;

//DispatchRequest.java (nouveau)
@Data
public class DispatchRequest {
 private String targetDriver; // "all" ou ID numérique du chauffeur
}