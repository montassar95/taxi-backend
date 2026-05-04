package com.nck.taxi.controller;

import com.nck.taxi.dto.B2BRequestDto;
import com.nck.taxi.dto.AdminResponseDto;
import com.nck.taxi.model.B2BRequest;
import com.nck.taxi.service.B2BRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/b2b")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class B2BRequestController {
    private final B2BRequestService b2bRequestService;

    @PostMapping
    public ResponseEntity<B2BRequest> create(@RequestBody B2BRequestDto dto) {
        return ResponseEntity.ok(b2bRequestService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<B2BRequest>> getAll() {
        return ResponseEntity.ok(b2bRequestService.getAll());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<B2BRequest> updateStatus(
            @PathVariable Long id,
            @RequestBody AdminResponseDto dto) {
        return ResponseEntity.ok(b2bRequestService.updateStatus(id, dto));
    }
}