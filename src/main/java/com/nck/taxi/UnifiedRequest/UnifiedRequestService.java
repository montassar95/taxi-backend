package com.nck.taxi.UnifiedRequest;

 

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nck.taxi.dto.AdminResponseDto;
import com.nck.taxi.model.B2BRequest;
import com.nck.taxi.model.Booking;
import com.nck.taxi.model.EventRequest;
import com.nck.taxi.repository.B2BRequestRepository;
import com.nck.taxi.repository.BookingRepository;
import com.nck.taxi.repository.EventRequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UnifiedRequestService {

    private final BookingRepository      bookingRepository;
    private final EventRequestRepository eventRepository;
    private final B2BRequestRepository   b2bRepository;

    // ─────────────────────────────────────────────────────────────────────────
    // GET ALL  (triés par date décroissante)
    // ─────────────────────────────────────────────────────────────────────────
    public List<UnifiedRequestDto> getAll() {
        List<UnifiedRequestDto> result = new ArrayList<>();

        bookingRepository.findAll().forEach(b -> result.add(fromBooking(b)));
        eventRepository.findAll().forEach(e   -> result.add(fromEvent(e)));
        b2bRepository.findAll().forEach(b     -> result.add(fromB2B(b)));

        result.sort(Comparator.comparing(UnifiedRequestDto::getCreatedAt).reversed());
        return result;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // UPDATE STATUS
    //   Reçoit CONFIRMED ou CANCELLED (normalisé front)
    //   et mappe vers les enums DB corrects selon le type
    // ─────────────────────────────────────────────────────────────────────────
    public UnifiedRequestDto updateStatus(String type, Long id, AdminResponseDto dto) {
        String normalized = dto.getStatus(); // CONFIRMED | CANCELLED

        return switch (type) {
            case "booking" -> {
                Booking b = bookingRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + id));
                b.setStatus(Booking.BookingStatus.valueOf(normalized)); // CONFIRMED | CANCELLED
                yield fromBooking(bookingRepository.save(b));
            }
            case "event" -> {
                EventRequest e = eventRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Event not found: " + id));
                // EventRequest utilise ACCEPTED/REFUSED dans la DB
                e.setStatus("CONFIRMED".equals(normalized)
                        ? EventRequest.RequestStatus.ACCEPTED
                        : EventRequest.RequestStatus.REFUSED);
                yield fromEvent(eventRepository.save(e));
            }
            case "b2b" -> {
                B2BRequest b = b2bRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("B2B not found: " + id));
                b.setStatus("CONFIRMED".equals(normalized)
                        ? B2BRequest.RequestStatus.ACCEPTED
                        : B2BRequest.RequestStatus.REFUSED);
                yield fromB2B(b2bRepository.save(b));
            }
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Normalisation statut : ACCEPTED → CONFIRMED, REFUSED → CANCELLED
    // ─────────────────────────────────────────────────────────────────────────
    private String normalizeStatus(String raw) {
        return switch (raw) {
            case "ACCEPTED" -> "CONFIRMED";
            case "REFUSED"  -> "CANCELLED";
            default         -> raw; // PENDING, CONFIRMED, CANCELLED déjà corrects
        };
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Mappers
    // ─────────────────────────────────────────────────────────────────────────
    private UnifiedRequestDto fromBooking(Booking b) {
        UnifiedRequestDto dto = new UnifiedRequestDto();
        dto.setId(b.getId());
        dto.setType("booking");
        dto.setStatus(normalizeStatus(b.getStatus().name()));
        dto.setCreatedAt(b.getCreatedAt().toString());
        dto.setName(b.getFirstName() + " " + b.getLastName());
        dto.setPhone(b.getPhone());
        dto.setEmail(b.getEmail());
        dto.setComment(b.getComment());
        dto.setPickup(b.getPickupAddress());
        dto.setDropoff(b.getDropoffAddress());
        dto.setRideDate(b.getBookingDateTime() != null ? b.getBookingDateTime().toString() : null);
        dto.setTripType(b.getTripType());
        return dto;
    }

    private UnifiedRequestDto fromEvent(EventRequest e) {
        UnifiedRequestDto dto = new UnifiedRequestDto();
        dto.setId(e.getId());
        dto.setType("event");
        dto.setStatus(normalizeStatus(e.getStatus().name()));
        dto.setCreatedAt(e.getCreatedAt().toString());
        dto.setName(e.getFirstName() + " " + e.getLastName());
        dto.setPhone(e.getPhone());
        dto.setEmail(e.getEmail());
        dto.setComment(e.getComment());
        dto.setEventType(e.getEventType());
        dto.setEventDate(e.getEventDate()  != null ? e.getEventDate().toString()  : null);
        dto.setStartTime(e.getStartTime()  != null ? e.getStartTime().toString()  : null);
        dto.setNumberOfPersons(e.getNumberOfPersons());
        dto.setNumberOfTaxis(e.getNumberOfTaxis());
        dto.setTripType(e.getTripType());
        return dto;
    }

    private UnifiedRequestDto fromB2B(B2BRequest b) {
        UnifiedRequestDto dto = new UnifiedRequestDto();
        dto.setId(b.getId());
        dto.setType("b2b");
        dto.setStatus(normalizeStatus(b.getStatus().name()));
        dto.setCreatedAt(b.getCreatedAt().toString());
        dto.setName(b.getContactName());
        dto.setPhone(b.getPhone());
        dto.setEmail(b.getEmail());
        dto.setComment(b.getComment());
        dto.setCompanyName(b.getCompanyName());
        dto.setContactName(b.getContactName());
        dto.setAddress(b.getAddress());
        dto.setCity(b.getCity());
        dto.setServiceType(b.getServiceType());
        dto.setDailyRides(b.getDailyRides());
        dto.setFrequency(b.getFrequency());
        dto.setSchedule(b.getSchedule());
        dto.setUsersCount(b.getUsersCount());
        return dto;
    }
}
