package com.nck.taxi.b2b.dto;

 

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class B2BGroupResponse {
    private Long                  id;
    private String                label;
    private String                date;
    private String                heure;
    private List<B2BCourseResponse> courses;
    private LocalDateTime         createdAt;
}