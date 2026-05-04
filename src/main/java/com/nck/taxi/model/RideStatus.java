package com.nck.taxi.model;

public enum RideStatus {
    PENDING,      // En attente
    ACCEPTED,     // Acceptée par un chauffeur
     IN_PROGRESS,  // En cours
    DONE,         // Terminée
    CANCELLED,    // Annulée par chauffeur
    SPONTANEOUS   // Course spontanée
}