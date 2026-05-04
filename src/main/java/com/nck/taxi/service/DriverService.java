package com.nck.taxi.service;

import com.nck.taxi.model.Driver;
import com.nck.taxi.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    // Récupérer tous les chauffeurs
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    // Récupérer un chauffeur par ID
    public Driver getDriverById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé"));
    }

    // Mettre à jour disponibilité
    public Driver updateAvailability(Long id, boolean available) {
        Driver driver = getDriverById(id);
        driver.setAvailable(available);
        return driverRepository.save(driver);
    }

    // Récupérer chauffeurs disponibles
    public List<Driver> getAvailableDrivers() {
        return driverRepository.findAll()
                .stream()
                .filter(Driver::isAvailable)
                .toList();
    }
    
    public Driver setDriverAvailability(Long driverId, boolean available) {

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé"));

        driver.setAvailable(available);

        return driverRepository.save(driver);
    }
}