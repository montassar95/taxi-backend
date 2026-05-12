package com.nck.taxi.b2b.service;
 

import com.nck.taxi.b2b.dto.AdminCompanyDTO;
import com.nck.taxi.b2b.dto.AdminCourseDTO;
import com.nck.taxi.b2b.model.B2BCompany;
import com.nck.taxi.b2b.model.B2BCompany.CompanyStatus;
import com.nck.taxi.b2b.model.B2BCourse;
import com.nck.taxi.b2b.model.B2BCourse.CourseStatus;
import com.nck.taxi.b2b.repository.B2BCompanyRepository;
import com.nck.taxi.b2b.repository.B2BCourseRepository;
import com.nck.taxi.b2b.repository.B2BGroupRepository;
import com.nck.taxi.model.Driver;
import com.nck.taxi.model.Ride;
import com.nck.taxi.model.RideStatus;
import com.nck.taxi.repository.DriverRepository;
import com.nck.taxi.repository.RideRepository;
import com.nck.taxi.service.RideService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class B2BAdminService {

    private final B2BCompanyRepository companyRepo;
    private final B2BCourseRepository  courseRepo;
    private final B2BGroupRepository   groupRepo;

    
    private final RideRepository    rideRepository;
    private final DriverRepository  driverRepository;
    private final RideService       rideService;
    /* ════════════════════════════════
       ENTREPRISES
    ════════════════════════════════ */

   
    @Transactional
    public AdminCompanyDTO updateStatus(Long id, String status) {
        B2BCompany company = companyRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found: " + id));
        company.setStatus(CompanyStatus.valueOf(status));
        return toAdminCompanyDTO(companyRepo.save(company));
    }

    @Transactional
    public void deleteCompany(Long id) {
        companyRepo.deleteById(id);
    }

    /* ════════════════════════════════
       COURSES
    ════════════════════════════════ */

   

    @Transactional
    public AdminCourseDTO updateCourseStatus(Long id, String status) {
        B2BCourse course = courseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found: " + id));
        course.setStatus(CourseStatus.valueOf(status));
        return toAdminCourseDTO(courseRepo.save(course));
    }

    @Transactional
    public void deleteCourse(Long id) {
        courseRepo.deleteById(id);
    }

    /* ════════════════════════════════
       STATS
    ════════════════════════════════ */

  

    /* ════════════════════════════════
       MAPPERS
    ════════════════════════════════ */

    private AdminCompanyDTO toAdminCompanyDTO(B2BCompany c) {
        return AdminCompanyDTO.builder()
                .id(c.getId())
                .companyName(c.getCompanyName())
                .email(c.getEmail())
                .phone(c.getPhone())
                .address(c.getAddress())
                .status(c.getStatus())
                .createdAt(c.getCreatedAt())
                .coursesCount(courseRepo.countByCompany(c))
                .groupsCount(groupRepo.countByCompany(c))
                .build();
    }
    @Transactional
    public AdminCourseDTO dispatchToRide(Long courseId, String targetDriver) {

        B2BCourse b2b = courseRepo.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));

        if (b2b.getStatus() != B2BCourse.CourseStatus.CONFIRMEE) {
            throw new RuntimeException("Seules les courses CONFIRMEES peuvent être dispatchées");
        }

        // ── 1. Construire le Ride ──────────────────────────────────
        LocalDateTime rideDateTime;
        try {
            rideDateTime = LocalDateTime.parse(b2b.getDate() + "T" + b2b.getHeure());
        } catch (Exception e) {
            rideDateTime = LocalDateTime.now();
        }

        Ride ride = new Ride();
        ride.setPassengerName(b2b.getPrenom() + " " + b2b.getNom());
        ride.setPickupAddress(b2b.getAdresseDepart());
        ride.setDropoffAddress(b2b.getDestination());
        ride.setRideDateTime(rideDateTime);
        ride.setStatus(RideStatus.PENDING);
        ride.setB2b(true);
        ride.setB2bSource(b2b);             // ← lien Ride → B2BCourse
        ride.setB2bCompany(b2b.getCompany());
        Ride savedRide = rideRepository.save(ride);

        // ── 2. Lien inverse B2BCourse → Ride ──────────────────────
        b2b.setLinkedRide(savedRide);
        b2b.setStatus(B2BCourse.CourseStatus.EN_COURS);
        courseRepo.save(b2b);

        // ── 3. Dispatch — même logique que B2C ────────────────────
        if ("all".equals(targetDriver)) {
            rideService.broadcastRideToAllDrivers(savedRide);
        } else {
            try {
                Long driverId = Long.parseLong(targetDriver);
                Driver driver = driverRepository.findById(driverId)
                    .orElseThrow(() -> new RuntimeException("Driver not found"));
                rideService.sendRideToDriver(driver.getId(), savedRide);
            } catch (NumberFormatException e) {
                throw new RuntimeException("targetDriver invalide : " + targetDriver);
            }
        }

        return toAdminCourseDTO(b2b);
    }

//    private AdminCourseDTO toAdminCourseDTO(B2BCourse c) {
//        return AdminCourseDTO.builder()
//                .id(c.getId())
//                .companyName(c.getCompany() != null
//                        ? c.getCompany().getCompanyName() : "—")
//                .companyId(c.getCompany() != null
//                        ? c.getCompany().getId() : null)
//                .nom(c.getNom())
//                .prenom(c.getPrenom())
//                .telephone(c.getTelephone())
//                .adresseDepart(c.getAdresseDepart())
//                .destination(c.getDestination())
//                .date(c.getDate())
//                .heure(c.getHeure())
//                .notes(c.getNotes())
//                .status(c.getStatus())
//                .createdAt(c.getCreatedAt())
//                .rideId(c.getLinkedRide() != null ? c.getLinkedRide().getId() : null)
//                .build();
//    }
//    
    
    
    
    private AdminCourseDTO toAdminCourseDTO(B2BCourse c) {
        
        // Récupérer le driver via le Ride lié
        String driverName = null;
        if (c.getLinkedRide() != null) {
            Ride ride = c.getLinkedRide();
            // Recharger le ride avec le driver (LAZY)
            Ride freshRide = rideRepository.findById(ride.getId()).orElse(null);
            if (freshRide != null && freshRide.getDriver() != null) {
                driverName = freshRide.getDriver().getName();
            }
        }

        return AdminCourseDTO.builder()
                .id(c.getId())
                .companyName(c.getCompany() != null ? c.getCompany().getCompanyName() : "—")
                .companyId(c.getCompany()   != null ? c.getCompany().getId()          : null)
                .nom(c.getNom())
                .prenom(c.getPrenom())
                .telephone(c.getTelephone())
                .adresseDepart(c.getAdresseDepart())
                .destination(c.getDestination())
                .date(c.getDate())
                .heure(c.getHeure())
                .notes(c.getNotes())
                .status(c.getStatus())
                .createdAt(c.getCreatedAt())
                .rideId(c.getLinkedRide()     != null ? c.getLinkedRide().getId()                : null)
                .rideStatus(c.getLinkedRide() != null ? c.getLinkedRide().getStatus().name()     : null)
                .driverName(driverName)               // ← branché ici
                .build();
    }
    
    
    
    
    
    
    
    
    
    @Transactional(readOnly = true)
    public List<AdminCompanyDTO> getAllCompanies() {
        return companyRepo.findAll()
                .stream()
                .map(this::toAdminCompanyDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdminCompanyDTO getCompany(Long id) {
        B2BCompany company = companyRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found: " + id));
        return toAdminCompanyDTO(company);
    }

    @Transactional(readOnly = true)
    public List<AdminCourseDTO> getAllCourses() {
        return courseRepo.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toAdminCourseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AdminCourseDTO> getCoursesByCompany(Long companyId) {
        B2BCompany company = companyRepo.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found: " + companyId));
        return courseRepo.findByCompanyOrderByCreatedAtDesc(company)
                .stream()
                .map(this::toAdminCourseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public java.util.Map<String, Long> getStats() {
        long totalCompanies = companyRepo.count();
        long totalCourses   = courseRepo.count();
        long enAttente      = courseRepo.findAllByOrderByCreatedAtDesc()
                .stream()
                .filter(c -> c.getStatus() == B2BCourse.CourseStatus.EN_ATTENTE)
                .count();
        long actives = companyRepo.findAll()
                .stream()
                .filter(c -> c.getStatus() == B2BCompany.CompanyStatus.ACTIVE)
                .count();

        return java.util.Map.of(
            "totalCompanies",  totalCompanies,
            "totalCourses",    totalCourses,
            "enAttente",       enAttente,
            "activeCompanies", actives
        );
    }
}
