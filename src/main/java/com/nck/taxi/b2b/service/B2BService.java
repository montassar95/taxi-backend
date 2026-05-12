package com.nck.taxi.b2b.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nck.taxi.b2b.dto.B2BAuthResponse;
import com.nck.taxi.b2b.dto.B2BCompanyDTO;
import com.nck.taxi.b2b.dto.B2BCourseRequest;
import com.nck.taxi.b2b.dto.B2BCourseResponse;
import com.nck.taxi.b2b.dto.B2BGroupRequest;
import com.nck.taxi.b2b.dto.B2BGroupResponse;
import com.nck.taxi.b2b.dto.B2BLoginRequest;
import com.nck.taxi.b2b.dto.B2BRegisterRequest;
import com.nck.taxi.b2b.model.B2BCompany;
import com.nck.taxi.b2b.model.B2BCourse;
import com.nck.taxi.b2b.model.B2BGroup;
import com.nck.taxi.b2b.repository.B2BCompanyRepository;
import com.nck.taxi.b2b.repository.B2BCourseRepository;
import com.nck.taxi.b2b.repository.B2BGroupRepository;
import com.nck.taxi.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class B2BService {

    private final B2BCompanyRepository companyRepo;
    private final B2BCourseRepository  courseRepo;
    private final B2BGroupRepository   groupRepo;
    private final PasswordEncoder      passwordEncoder;
    private final JwtUtil              jwtUtil;

    /* ════════════════════════════════
       AUTH
    ════════════════════════════════ */

    @Transactional
    public B2BAuthResponse register(B2BRegisterRequest req) {
        if (companyRepo.existsByEmail(req.getEmail())) {
            throw new RuntimeException("EMAIL_EXISTS");
        }

        B2BCompany company = B2BCompany.builder()
                .companyName(req.getCompanyName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .phone(req.getPhone())
                .address(req.getAddress())
                .status(B2BCompany.CompanyStatus.ACTIVE)
                .build();

        company = companyRepo.save(company);

        String token = jwtUtil.generateB2BToken(company.getEmail(), company.getId());

        return B2BAuthResponse.builder()
                .token(token)
                .company(toCompanyDTO(company))
                .build();
    }

    public B2BAuthResponse login(B2BLoginRequest req) {
        B2BCompany company = companyRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("INVALID_CREDENTIALS"));

        if (!passwordEncoder.matches(req.getPassword(), company.getPassword())) {
            throw new RuntimeException("INVALID_CREDENTIALS");
        }

        if (company.getStatus() == B2BCompany.CompanyStatus.SUSPENDED) {
            throw new RuntimeException("ACCOUNT_SUSPENDED");
        }

        String token = jwtUtil.generateB2BToken(company.getEmail(), company.getId());

        return B2BAuthResponse.builder()
                .token(token)
                .company(toCompanyDTO(company))
                .build();
    }

    /* ════════════════════════════════
       COURSES
    ════════════════════════════════ */

    @Transactional
    public B2BCourseResponse addCourse(B2BCourseRequest req, String email) {
        B2BCompany company = companyRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        B2BCourse course = B2BCourse.builder()
                .company(company)
                .nom(req.getNom())
                .prenom(req.getPrenom())
                .telephone(req.getTelephone())
                .adresseDepart(req.getAdresseDepart())
                .destination(req.getDestination())
                .date(req.getDate())
                .heure(req.getHeure())
                .notes(req.getNotes())
                .status(B2BCourse.CourseStatus.EN_ATTENTE)
                .build();

        return toCourseResponse(courseRepo.save(course));
    }

    public List<B2BCourseResponse> getMyCourses(String email) {
        B2BCompany company = companyRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        return courseRepo
                .findByGroupIsNullAndCompanyOrderByCreatedAtDesc(company)
                .stream()
                .map(this::toCourseResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteCourse(Long id, String email) {
        B2BCourse course = courseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!course.getCompany().getEmail().equals(email)) {
            throw new RuntimeException("FORBIDDEN");
        }

        courseRepo.delete(course);
    }

    /* ════════════════════════════════
       GROUPES
    ════════════════════════════════ */

    @Transactional
    public B2BGroupResponse addGroup(B2BGroupRequest req, String email) {
        B2BCompany company = companyRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        B2BGroup group = B2BGroup.builder()
                .company(company)
                .label(req.getLabel())
                .date(req.getDate())
                .heure(req.getHeure())
                .build();

        group = groupRepo.save(group);

        final B2BGroup savedGroup = group;

        List<B2BCourse> courses = req.getCourses().stream().map(c ->
                B2BCourse.builder()
                        .company(company)
                        .group(savedGroup)
                        .nom(c.getNom())
                        .prenom(c.getPrenom())
                        .telephone(c.getTelephone())
                        .adresseDepart(c.getAdresseDepart())
                        .destination(c.getDestination())
                        .date(c.getDate() != null && !c.getDate().isEmpty()
                                ? c.getDate() : req.getDate())
                        .heure(c.getHeure() != null && !c.getHeure().isEmpty()
                                ? c.getHeure() : req.getHeure())
                        .notes(c.getNotes())
                        .status(B2BCourse.CourseStatus.EN_ATTENTE)
                        .build()
        ).collect(Collectors.toList());

        courseRepo.saveAll(courses);
        group.setCourses(courses);

        return toGroupResponse(group);
    }

    public List<B2BGroupResponse> getMyGroups(String email) {
        B2BCompany company = companyRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        return groupRepo
                .findByCompanyOrderByCreatedAtDesc(company)
                .stream()
                .map(this::toGroupResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteGroup(Long id, String email) {
        B2BGroup group = groupRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (!group.getCompany().getEmail().equals(email)) {
            throw new RuntimeException("FORBIDDEN");
        }

        groupRepo.delete(group);
    }

    /* ════════════════════════════════
       MAPPERS
    ════════════════════════════════ */

    private B2BCompanyDTO toCompanyDTO(B2BCompany c) {
        return B2BCompanyDTO.builder()
                .id(c.getId())
                .companyName(c.getCompanyName())
                .email(c.getEmail())
                .phone(c.getPhone())
                .address(c.getAddress())
                .build();
    }

    private B2BCourseResponse toCourseResponse(B2BCourse c) {
        return B2BCourseResponse.builder()
                .id(c.getId())
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
                .build();
    }

    private B2BGroupResponse toGroupResponse(B2BGroup g) {
        return B2BGroupResponse.builder()
                .id(g.getId())
                .label(g.getLabel())
                .date(g.getDate())
                .heure(g.getHeure())
                .createdAt(g.getCreatedAt())
                .courses(g.getCourses().stream()
                        .map(this::toCourseResponse)
                        .collect(Collectors.toList()))
                .build();
    }
}