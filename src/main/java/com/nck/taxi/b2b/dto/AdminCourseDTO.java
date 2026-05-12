package com.nck.taxi.b2b.dto;
 

import com.nck.taxi.b2b.model.B2BCourse.CourseStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class AdminCourseDTO {
    private Long         id;
    private String       companyName;
    private Long         companyId;
    private String       nom;
    private String       prenom;
    private String       telephone;
    private String       adresseDepart;
    private String       destination;
    private String       date;
    private String       heure;
    private String       notes;
    private CourseStatus status;
    private LocalDateTime createdAt;
    
    private Long   rideId;       // ← null si pas encore dispatchée
    private String rideStatus;   // ← optionnel, pour affichage live
    
    private String driverName; 
}