package com.nck.taxi.contact;
 
import lombok.Data;

@Data
public class ContactRequestDTO {
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String sujet;
    private String message;
    private boolean urgent;
}
