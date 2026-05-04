package com.nck.taxi.service;

import com.nck.taxi.dto.B2BRequestDto;
import com.nck.taxi.dto.AdminResponseDto;
import com.nck.taxi.model.B2BRequest;
import com.nck.taxi.repository.B2BRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class B2BRequestService {

    private final B2BRequestRepository repo;

    public B2BRequest create(B2BRequestDto dto) {
        B2BRequest b = new B2BRequest();
        b.setCompanyName(dto.getCompanyName());
        b.setContactName(dto.getContactName());
        b.setPhone(dto.getPhone());
        b.setEmail(dto.getEmail());
        b.setAddress(dto.getAddress());
        b.setCity(dto.getCity());
        b.setServiceType(dto.getServiceType());
        b.setDailyRides(dto.getDailyRides());
        b.setFrequency(dto.getFrequency());
        b.setSchedule(dto.getSchedule());
        b.setUsersCount(dto.getUsersCount());
        b.setComment(dto.getComment());
        return repo.save(b);
    }

    public List<B2BRequest> getAll() {
        return repo.findAllByOrderByCreatedAtDesc();
    }

    public B2BRequest updateStatus(Long id, AdminResponseDto dto) {
        B2BRequest b = repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Non trouvé"));
        b.setStatus(B2BRequest.RequestStatus.valueOf(dto.getStatus()));
        b.setAdminResponse(dto.getResponse());
        return repo.save(b);
    }
}