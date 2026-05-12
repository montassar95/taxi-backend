package com.nck.taxi.b2b.controller;
 

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nck.taxi.b2b.dto.AdminCompanyDTO;
import com.nck.taxi.b2b.dto.AdminCourseDTO;
import com.nck.taxi.b2b.dto.DispatchRequest;
import com.nck.taxi.b2b.dto.StatusUpdateRequest;
import com.nck.taxi.b2b.service.B2BAdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/b2b")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class B2BAdminController {

    private final B2BAdminService adminService;

    /* ════════════════════════════════
       STATS
    ════════════════════════════════ */

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        return ResponseEntity.ok(adminService.getStats());
    }

    /* ════════════════════════════════
       ENTREPRISES
    ════════════════════════════════ */

    @GetMapping("/companies")
    public ResponseEntity<List<AdminCompanyDTO>> getAllCompanies() {
        return ResponseEntity.ok(adminService.getAllCompanies());
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<AdminCompanyDTO> getCompany(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getCompany(id));
    }

    @PatchMapping("/companies/{id}/status")
    public ResponseEntity<AdminCompanyDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest req) {
        return ResponseEntity.ok(adminService.updateStatus(id, req.getStatus()));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        adminService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    /* ════════════════════════════════
       COURSES
    ════════════════════════════════ */

    @GetMapping("/courses")
    public ResponseEntity<List<AdminCourseDTO>> getAllCourses() {
        return ResponseEntity.ok(adminService.getAllCourses());
    }

    @GetMapping("/companies/{id}/courses")
    public ResponseEntity<List<AdminCourseDTO>> getCoursesByCompany(
            @PathVariable Long id) {
        return ResponseEntity.ok(adminService.getCoursesByCompany(id));
    }

    @PatchMapping("/courses/{id}/status")
    public ResponseEntity<AdminCourseDTO> updateCourseStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest req) {
        return ResponseEntity.ok(adminService.updateCourseStatus(id, req.getStatus()));
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        adminService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
    
    
 // B2BAdminController.java
    @PostMapping("/courses/{id}/dispatch")
    public ResponseEntity<AdminCourseDTO> dispatch(
            @PathVariable Long id,
            @RequestBody DispatchRequest req) {
        return ResponseEntity.ok(adminService.dispatchToRide(id, req.getTargetDriver()));
    }
}