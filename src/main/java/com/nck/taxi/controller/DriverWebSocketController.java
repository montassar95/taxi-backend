package com.nck.taxi.controller;

import com.nck.taxi.dto.PositionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class DriverWebSocketController {

    // Reçoit la position GPS du chauffeur via WebSocket
    @MessageMapping("/driver/position")
    public void receivePosition(PositionDto position) {
        System.out.printf(
            "📍 Chauffeur %s → Lat: %f, Lng: %f%n",
            position.getDriverId(),
            position.getLat(),
            position.getLng()
        );
        // Ici tu pourras sauvegarder en DB ou broadcaster
    }

    // Reçoit acceptation course via WebSocket
    @MessageMapping("/ride/accept")
    public void acceptRideWs(com.nck.taxi.dto.RideDto dto) {
        System.out.printf(
            "✅ Chauffeur %s accepte course %s%n",
            dto.getDriverId(),
            dto.getRideId()
        );
    }
}