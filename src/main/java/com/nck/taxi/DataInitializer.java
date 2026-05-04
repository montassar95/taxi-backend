package com.nck.taxi;

import com.nck.taxi.model.Driver;
import com.nck.taxi.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (driverRepository.count() == 0) {

            // ── Chauffeur test ──
            Driver driver1 = new Driver();
            driver1.setUsername("chauffeur1");
            driver1.setPassword(passwordEncoder.encode("password123"));
            driver1.setName("Mohamed Ali");
            driver1.setAvailable(true);
            driver1.setRole(Driver.Role.DRIVER);
            driverRepository.save(driver1);

            
            Driver driver2 = new Driver();
            driver2.setUsername("chauffeur2");
            driver2.setPassword(passwordEncoder.encode("password123"));
            driver2.setName("Khaled");
            driver2.setAvailable(true);
            driver2.setRole(Driver.Role.DRIVER);
            driverRepository.save(driver2);
            
            
            // ── Admin ──
            Driver admin = new Driver();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin2024"));
            admin.setName("Administrateur NCK");
            admin.setAvailable(false);
            admin.setRole(Driver.Role.ADMIN);
            driverRepository.save(admin);

            System.out.println("✅ Chauffeur : chauffeur1 / password123");
            System.out.println("✅ Admin     : admin / admin2024");
        }
    }
}