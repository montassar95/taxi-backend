package com.nck.taxi.location;

 
 
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nck.taxi.model.Driver;
import com.nck.taxi.model.RideStatus;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/locations")
public class LocationController {

    // cache mémoire simple (test sans DB)
    private final ConcurrentHashMap<String, String> lastPositions = new ConcurrentHashMap<>();

//    @PostMapping
//    public ResponseEntity<Void> receiveLocation(@RequestBody LocationRequestDTO dto) {
//
//        System.out.println("📍 GPS reçu:");
//        System.out.println("Driver: " + dto.getDriverId());
//        System.out.println("Lat: " + dto.getLat());
//        System.out.println("Lng: " + dto.getLng());
//
//        // stockage temporaire (test)
//        lastPositions.put(dto.getDriverId(),
//                dto.getLat() + "," + dto.getLng());
//
//        return ResponseEntity.ok().build();
//    }
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
//    @PostMapping
//    public ResponseEntity<Void> receiveLocation(@RequestBody LocationRequestDTO dto) {
//
//        System.out.println("📍 GPS reçu:");
//        System.out.println("Driver: " + dto.getDriverId());
//
//        // 1. stocker en mémoire
//        lastPositions.put(dto.getDriverId(),
//                dto.getLat() + "," + dto.getLng());
//
//        // 2. envoyer en temps réel à Angular
//        messagingTemplate.convertAndSend(
//                "/topic/locations",
//                dto
//        );
//
//        return ResponseEntity.ok().build();
//    }
    
    @PostMapping
  
    public ResponseEntity<Void> receiveLocation(@RequestBody LocationRequestDTO dto) {
    	
    	        
        System.out.println("📍 GPS reçu: " + dto.getDriverId());
        System.out.println("   name   : " + dto.getName());
        System.out.println("   status   : " + dto.getStatus());
        System.out.println("   speed   : " + dto.getSpeed());
         System.out.println("   lat    : " + dto.getLat());
        System.out.println("   lng    : " + dto.getLng());

        lastPositions.put(dto.getDriverId(), dto.getLat() + "," + dto.getLng());

        messagingTemplate.convertAndSend("/topic/locations", dto);
        System.out.println("✅ Broadcast envoyé sur /topic/locations");

        return ResponseEntity.ok().build();
    }
    
    
    // endpoint test pour vérifier que ça marche
    @GetMapping("/last/{driverId}")
    public ResponseEntity<String> getLast(@PathVariable String driverId) {
        return ResponseEntity.ok(lastPositions.get(driverId));
    }
}
