package com.nck.taxi.contact;

 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nck.taxi.contact.ContactMessage.StatutMessage;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

    List<ContactMessage> findAllByOrderByCreatedAtDesc();

    List<ContactMessage> findByStatutOrderByCreatedAtDesc(StatutMessage statut);

    long countByStatut(StatutMessage statut);
}