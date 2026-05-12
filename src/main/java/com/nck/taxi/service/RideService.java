package com.nck.taxi.service;

import com.nck.taxi.dto.CancelRideDto;
import com.nck.taxi.dto.SpontaneousRideDto;
import com.nck.taxi.model.Driver;
import com.nck.taxi.model.Ride;
import com.nck.taxi.model.RideStatus;
import com.nck.taxi.repository.DriverRepository;
import com.nck.taxi.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RideService {

    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // Envoie une course à un chauffeur
    public void sendRideToDriver(Long driverId, Ride ride) {
        messagingTemplate.convertAndSend(
            "/topic/driver/" + driverId + "/rides", ride
        );
    }

    // Broadcast à TOUS les chauffeurs disponibles
    public void broadcastRideToAllDrivers(Ride ride) {
        List<Driver> availableDrivers = driverRepository.findAll()
            .stream()
            .filter(Driver::isAvailable)
            .toList();

        for (Driver driver : availableDrivers) {
            messagingTemplate.convertAndSend(
                "/topic/driver/" + driver.getId() + "/rides", ride
            );
        }
        System.out.println("📢 Course broadcastée à " + availableDrivers.size() + " chauffeurs");
    }

    // Notifie tous les chauffeurs que la course est prise
    public void notifyRideTaken(Long rideId) {
        messagingTemplate.convertAndSend(
            "/topic/rides/taken",
            rideId.toString()
        );
        System.out.println("🚫 Course " + rideId + " prise — notifie tous les chauffeurs");
    }

    // Accepter une course
    public Ride acceptRide(Long rideId, Long driverId) {
        Ride ride = rideRepository.findById(rideId)
            .orElseThrow(() -> new RuntimeException("Course non trouvée"));

        // Déjà acceptée par quelqu'un d'autre
        if (ride.getStatus() == RideStatus.ACCEPTED) {
            throw new RuntimeException("Course déjà acceptée");
        }

        Driver driver = driverRepository.findById(driverId)
            .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé"));

        ride.setStatus(RideStatus.ACCEPTED);
        ride.setDriver(driver);
        ride.setAcceptedAt(LocalDateTime.now());
        driver.setAvailable(false);

        driverRepository.save(driver);
        Ride saved = rideRepository.save(ride);

        // Notifie tous les autres chauffeurs
        notifyRideTaken(rideId);

        return saved;
    }

   // Démarrer la course
    public Ride startRide(Long rideId, Long driverId) {

        Ride ride = rideRepository.findById(rideId)
            .orElseThrow(() -> new RuntimeException("Course non trouvée"));

        Driver driver = driverRepository.findById(driverId)
            .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé"));

        if (ride.getStatus() != RideStatus.ACCEPTED) {
            throw new RuntimeException("La course doit être ACCEPTED pour démarrer");
        }

        ride.setStatus(RideStatus.IN_PROGRESS);

        return rideRepository.save(ride);
    }
    // Annuler une course en cours
    public Ride cancelRide(CancelRideDto dto) {
        Ride ride = rideRepository.findById(Long.parseLong(dto.getRideId()))
            .orElseThrow(() -> new RuntimeException("Course non trouvée"));

        Driver driver = driverRepository.findById(Long.parseLong(dto.getDriverId()))
            .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé"));

        ride.setStatus(RideStatus.CANCELLED);
        ride.setCancelledAt(LocalDateTime.now());
        ride.setCancellationReason(dto.getReason());
        driver.setAvailable(true);

        driverRepository.save(driver);
        Ride saved = rideRepository.save(ride);

        // Redistribue la course à tous les chauffeurs
        saved.setStatus(RideStatus.PENDING);
        broadcastRideToAllDrivers(saved);

        return saved;
    }

    // Créer course spontanée
    public Ride createSpontaneousRide(SpontaneousRideDto dto) {
        Driver driver = driverRepository.findById(Long.parseLong(dto.getDriverId()))
            .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé"));

        Ride ride = new Ride();
        ride.setPassengerName(dto.getPassengerName());
        ride.setPickupAddress(dto.getPickupAddress());
        ride.setDropoffAddress(dto.getDropoffAddress());
//        ride.setPickupLat(dto.getPickupLat());
//        ride.setPickupLng(dto.getPickupLng());
        ride.setRideDateTime(dto.getRideDateTime());
        ride.setDriver(driver);
        ride.setStatus(RideStatus.ACCEPTED);
        ride.setSpontaneous(true);
        ride.setAcceptedAt(LocalDateTime.now());
        driver.setAvailable(false);

        driverRepository.save(driver);
        return rideRepository.save(ride);
    }

    // Historique d'un chauffeur
    public List<Ride> getDriverHistory(Long driverId) {
        return rideRepository.findByDriverIdOrderByCreatedAtDesc(driverId);
    }

    // Terminer une course
    public Ride completeRide(Long rideId, Long driverId) {

        Ride ride = rideRepository.findById(rideId)
            .orElseThrow(() -> new RuntimeException("Course non trouvée"));

        Driver driver = driverRepository.findById(driverId)
            .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé"));

        // 🔒 sécurité métier
        if (ride.getStatus() != RideStatus.IN_PROGRESS) {
            throw new RuntimeException("La course doit être EN COURS pour être terminée");
        }

        ride.setStatus(RideStatus.DONE);
        driver.setAvailable(true);

        driverRepository.save(driver);
        return rideRepository.save(ride);
    }
    
    

    // Créer course test
    public Ride createRide(String passengerName, String pickupAddress,
                           String dropoffAddress) {
//    	, Double pickupLat, Double pickupLng
        Ride ride = new Ride();
        ride.setPassengerName(passengerName);
        ride.setPickupAddress(pickupAddress);
        ride.setDropoffAddress(dropoffAddress);
//        ride.setPickupLat(pickupLat);
//        ride.setPickupLng(pickupLng);
        ride.setStatus(RideStatus.PENDING);
        ride.setRideDateTime(LocalDateTime.now());
        return rideRepository.save(ride);
    }
    
    
    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    public Ride getRideById(Long id) {
        return rideRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Course non trouvée"));
    }
}