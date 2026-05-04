package com.nck.taxi.repository;

import com.nck.taxi.model.B2BRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface B2BRequestRepository extends JpaRepository<B2BRequest, Long> {
    List<B2BRequest> findAllByOrderByCreatedAtDesc();
    List<B2BRequest> findByStatusOrderByCreatedAtDesc(B2BRequest.RequestStatus status);
}