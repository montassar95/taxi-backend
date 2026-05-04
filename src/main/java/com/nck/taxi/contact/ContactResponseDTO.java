package com.nck.taxi.contact;
 
import java.time.LocalDateTime;

import com.nck.taxi.contact.ContactMessage.StatutMessage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactResponseDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String sujet;
    private String message;
    private boolean urgent;
    private StatutMessage statut;
    private LocalDateTime createdAt;
}