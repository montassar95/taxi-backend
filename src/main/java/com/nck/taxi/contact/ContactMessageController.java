package com.nck.taxi.contact;
 
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

import com.nck.taxi.contact.ContactMessage.StatutMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
public class ContactMessageController {

    private final ContactMessageService service;

    /* ── PUBLIC : envoi formulaire ── */
    @PostMapping
    public ResponseEntity<ContactResponseDTO> send(@RequestBody ContactRequestDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    /* ── ADMIN : liste tous ── */
    @GetMapping("/admin")
    public ResponseEntity<List<ContactResponseDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    /* ── ADMIN : filtrer par statut ── */
    @GetMapping("/admin/statut/{statut}")
    public ResponseEntity<List<ContactResponseDTO>> getByStatut(@PathVariable StatutMessage statut) {
        return ResponseEntity.ok(service.findByStatut(statut));
    }

    /* ── ADMIN : un message ── */
    @GetMapping("/admin/{id}")
    public ResponseEntity<ContactResponseDTO> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    /* ── ADMIN : changer statut ── */
    @PatchMapping("/admin/{id}/statut")
    public ResponseEntity<ContactResponseDTO> updateStatut(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        StatutMessage statut = StatutMessage.valueOf(body.get("statut"));
        return ResponseEntity.ok(service.updateStatut(id, statut));
    }

    /* ── ADMIN : supprimer ── */
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /* ── ADMIN : compteur non lus ── */
    @GetMapping("/admin/count/non-lus")
    public ResponseEntity<Map<String, Long>> countNonLus() {
        return ResponseEntity.ok(Map.of("count", service.countNonLus()));
    }
}
