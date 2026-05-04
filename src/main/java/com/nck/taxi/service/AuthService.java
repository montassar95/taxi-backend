package com.nck.taxi.service;

import com.nck.taxi.dto.LoginRequest;
import com.nck.taxi.dto.LoginResponse;
import com.nck.taxi.model.Driver;
import com.nck.taxi.repository.DriverRepository;
import com.nck.taxi.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

 
    
    
    
    public LoginResponse login(LoginRequest request) {
        Driver driver = driverRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(request.getPassword(), driver.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        String token = jwtUtil.generateToken(
                driver.getUsername(),
                driver.getId().toString(),
                driver.getRole().name()
        );

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setDriverId(driver.getId().toString());
        response.setName(driver.getName());
        response.setRole(driver.getRole().name());  // ← nouveau
        return response;
    }
}