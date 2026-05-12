package com.nck.taxi.b2b.dto;
 

import lombok.Data;

@Data
public class B2BCourseRequest {
    private String nom;
    private String prenom;
    private String telephone;
    private String adresseDepart;
    private String destination;
    private String date;
    private String heure;
    private String notes;
}