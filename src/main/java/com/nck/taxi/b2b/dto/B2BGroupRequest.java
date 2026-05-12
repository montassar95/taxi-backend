package com.nck.taxi.b2b.dto;
 
import java.util.List;

import lombok.Data;

@Data
public class B2BGroupRequest {
    private String             label;
    private String             date;
    private String             heure;
    private List<B2BCourseRequest> courses;
}