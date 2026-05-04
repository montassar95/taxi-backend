package com.nck.taxi.UnifiedRequest;

 

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nck.taxi.dto.AdminResponseDto;

import lombok.RequiredArgsConstructor;

/**
 * Endpoint unifié pour le tableau de bord admin.
 *
 * GET  /api/requests              → toutes les demandes (booking + event + b2b)
 *                                   triées par date décroissante, statuts normalisés
 *
 * PUT  /api/requests/{type}/{id}/status
 *       type = "booking" | "event" | "b2b"
 *       body = { "status": "CONFIRMED" | "CANCELLED" }
 *
 * Les anciens endpoints /api/bookings, /api/events, /api/b2b restent
 * disponibles pour la création côté client (formulaires publics).
 */
@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UnifiedRequestController {

    private final UnifiedRequestService unifiedRequestService;

    @GetMapping
    public ResponseEntity<List<UnifiedRequestDto>> getAll() {
        return ResponseEntity.ok(unifiedRequestService.getAll());
    }

    @PutMapping("/{type}/{id}/status")
    public ResponseEntity<UnifiedRequestDto> updateStatus(
            @PathVariable String type,
            @PathVariable Long id,
            @RequestBody AdminResponseDto dto) {
        return ResponseEntity.ok(unifiedRequestService.updateStatus(type, id, dto));
    }
}
