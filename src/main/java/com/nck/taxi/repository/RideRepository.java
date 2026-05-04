package com.nck.taxi.repository;

import com.nck.taxi.model.Ride;
import com.nck.taxi.model.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Long> {
    // Courses en attente
    List<Ride> findByStatus(RideStatus status);

    // Historique d'un chauffeur
    List<Ride> findByDriverIdOrderByCreatedAtDesc(Long driverId);

    // Courses spontanées d'un chauffeur
    List<Ride> findByDriverIdAndSpontaneousTrue(Long driverId);

    // Courses actives d'un chauffeur
    List<Ride> findByDriverIdAndStatusIn(Long driverId, List<RideStatus> statuses);
}