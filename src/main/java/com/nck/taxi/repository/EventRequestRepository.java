package com.nck.taxi.repository;

import com.nck.taxi.model.EventRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {
    List<EventRequest> findAllByOrderByCreatedAtDesc();
    List<EventRequest> findByStatusOrderByCreatedAtDesc(EventRequest.RequestStatus status);
}