package com.nck.taxi.controller;

import com.nck.taxi.dto.EventRequestDto;
import com.nck.taxi.dto.AdminResponseDto;
import com.nck.taxi.model.EventRequest;
import com.nck.taxi.service.EventRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EventRequestController {
    private final EventRequestService eventRequestService;

    @PostMapping
    public ResponseEntity<EventRequest> create(@RequestBody EventRequestDto dto) {
        return ResponseEntity.ok(eventRequestService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<EventRequest>> getAll() {
        return ResponseEntity.ok(eventRequestService.getAll());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<EventRequest> updateStatus(
            @PathVariable Long id,
            @RequestBody AdminResponseDto dto) {
        return ResponseEntity.ok(eventRequestService.updateStatus(id, dto));
    }
}