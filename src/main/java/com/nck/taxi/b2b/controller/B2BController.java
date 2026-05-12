package com.nck.taxi.b2b.controller;

 
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nck.taxi.b2b.dto.B2BCourseRequest;
import com.nck.taxi.b2b.dto.B2BCourseResponse;
import com.nck.taxi.b2b.dto.B2BGroupRequest;
import com.nck.taxi.b2b.dto.B2BGroupResponse;
import com.nck.taxi.b2b.dto.B2BLoginRequest;
import com.nck.taxi.b2b.dto.B2BRegisterRequest;
import com.nck.taxi.b2b.service.B2BService;
import com.nck.taxi.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/b2b")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class B2BController {

    private final B2BService b2bService;
    private final JwtUtil    jwtUtil;

    /* ── Helper : extraire email du token ── */
    private String emailFromHeader(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtUtil.extractB2BEmail(token);
    }

    /* ════════════════════════════════
       AUTH (public)
    ════════════════════════════════ */

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody B2BRegisterRequest req) {
        try {
            return ResponseEntity.ok(b2bService.register(req));
        } catch (RuntimeException e) {
            if ("EMAIL_EXISTS".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Un compte existe déjà avec cet email."));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur serveur."));
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody B2BLoginRequest req) {
        try {
            return ResponseEntity.ok(b2bService.login(req));
        } catch (RuntimeException e) {
            if ("INVALID_CREDENTIALS".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Email ou mot de passe incorrect."));
            }
            if ("ACCOUNT_SUSPENDED".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Compte suspendu. Contactez l'administrateur."));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur serveur."));
        }
    }

    /* ════════════════════════════════
       COURSES (authentifié B2B)
    ════════════════════════════════ */

    @PostMapping("/courses")
    public ResponseEntity<B2BCourseResponse> addCourse(
            @RequestBody B2BCourseRequest req,
            @RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(b2bService.addCourse(req, emailFromHeader(auth)));
    }

    @GetMapping("/courses")
    public ResponseEntity<List<B2BCourseResponse>> getMyCourses(
            @RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(b2bService.getMyCourses(emailFromHeader(auth)));
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable Long id,
            @RequestHeader("Authorization") String auth) {
        b2bService.deleteCourse(id, emailFromHeader(auth));
        return ResponseEntity.noContent().build();
    }

    /* ════════════════════════════════
       GROUPES (authentifié B2B)
    ════════════════════════════════ */

    @PostMapping("/groups")
    public ResponseEntity<B2BGroupResponse> addGroup(
            @RequestBody B2BGroupRequest req,
            @RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(b2bService.addGroup(req, emailFromHeader(auth)));
    }

    @GetMapping("/groups")
    public ResponseEntity<List<B2BGroupResponse>> getMyGroups(
            @RequestHeader("Authorization") String auth) {
        return ResponseEntity.ok(b2bService.getMyGroups(emailFromHeader(auth)));
    }

    @DeleteMapping("/groups/{id}")
    public ResponseEntity<Void> deleteGroup(
            @PathVariable Long id,
            @RequestHeader("Authorization") String auth) {
        b2bService.deleteGroup(id, emailFromHeader(auth));
        return ResponseEntity.noContent().build();
    }
}