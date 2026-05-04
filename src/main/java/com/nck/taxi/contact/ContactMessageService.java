package com.nck.taxi.contact;
 

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nck.taxi.contact.ContactMessage.StatutMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactMessageService {

    private final ContactMessageRepository repository;

    /* ── Enregistrer un message depuis le formulaire ── */
    public ContactResponseDTO save(ContactRequestDTO dto) {
        ContactMessage entity = ContactMessage.builder()
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .email(dto.getEmail())
                .telephone(dto.getTelephone())
                .sujet(dto.getSujet())
                .message(dto.getMessage())
                .urgent(dto.isUrgent())
                .statut(StatutMessage.NON_LU)
                .build();

        return toDTO(repository.save(entity));
    }

    /* ── Tous les messages (admin) ── */
    public List<ContactResponseDTO> findAll() {
        return repository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    /* ── Filtrer par statut ── */
    public List<ContactResponseDTO> findByStatut(StatutMessage statut) {
        return repository.findByStatutOrderByCreatedAtDesc(statut)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    /* ── Un seul message ── */
    public ContactResponseDTO findById(Long id) {
        ContactMessage msg = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message introuvable : " + id));
        // Marquer comme lu automatiquement
        if (msg.getStatut() == StatutMessage.NON_LU) {
            msg.setStatut(StatutMessage.LU);
            repository.save(msg);
        }
        return toDTO(msg);
    }

    /* ── Changer le statut ── */
    public ContactResponseDTO updateStatut(Long id, StatutMessage statut) {
        ContactMessage msg = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message introuvable : " + id));
        msg.setStatut(statut);
        return toDTO(repository.save(msg));
    }

    /* ── Supprimer ── */
    public void delete(Long id) {
        repository.deleteById(id);
    }

    /* ── Compteur non lus ── */
    public long countNonLus() {
        return repository.countByStatut(StatutMessage.NON_LU);
    }

    /* ── Mapper ── */
    private ContactResponseDTO toDTO(ContactMessage m) {
        return ContactResponseDTO.builder()
                .id(m.getId())
                .nom(m.getNom())
                .prenom(m.getPrenom())
                .email(m.getEmail())
                .telephone(m.getTelephone())
                .sujet(m.getSujet())
                .message(m.getMessage())
                .urgent(m.isUrgent())
                .statut(m.getStatut())
                .createdAt(m.getCreatedAt())
                .build();
    }
}
