package com.nck.taxi.b2b.dto;

import java.time.LocalDateTime;

import com.nck.taxi.b2b.model.B2BCourse.CourseStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class B2BCourseResponse {
    private Long        id;
    private String      nom;
    private String      prenom;
    private String      telephone;
    private String      adresseDepart;
    private String      destination;
    private String      date;
    private String      heure;
    private String      notes;
    private CourseStatus status;
    private LocalDateTime createdAt;
}