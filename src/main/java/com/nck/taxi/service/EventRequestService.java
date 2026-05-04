package com.nck.taxi.service;

import com.nck.taxi.dto.EventRequestDto;
import com.nck.taxi.dto.AdminResponseDto;
import com.nck.taxi.model.EventRequest;
import com.nck.taxi.repository.EventRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventRequestService {

    private final EventRequestRepository repo;

    public EventRequest create(EventRequestDto dto) {
        EventRequest e = new EventRequest();
        e.setFirstName(dto.getFirstName());
        e.setLastName(dto.getLastName());
        e.setPhone(dto.getPhone());
        e.setEmail(dto.getEmail());
        e.setEventType(dto.getEventType());
        e.setNumberOfPersons(dto.getNumberOfPersons());
        e.setNumberOfTaxis(dto.getNumberOfTaxis());
        e.setTripType(dto.getTripType());
        e.setComment(dto.getComment());

        if (dto.getEventDate() != null)
            e.setEventDate(LocalDate.parse(dto.getEventDate()));
        if (dto.getStartTime() != null)
            e.setStartTime(LocalTime.parse(dto.getStartTime()));

        return repo.save(e);
    }

    public List<EventRequest> getAll() {
        return repo.findAllByOrderByCreatedAtDesc();
    }

    public EventRequest updateStatus(Long id, AdminResponseDto dto) {
        EventRequest e = repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Non trouvé"));
        e.setStatus(EventRequest.RequestStatus.valueOf(dto.getStatus()));
        e.setAdminResponse(dto.getResponse());
        return repo.save(e);
    }
}