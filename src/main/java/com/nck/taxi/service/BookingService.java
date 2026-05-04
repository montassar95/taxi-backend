package com.nck.taxi.service;

import com.nck.taxi.dto.BookingRequest;
import com.nck.taxi.dto.AdminResponseDto;
import com.nck.taxi.model.Booking;
import com.nck.taxi.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    public Booking create(BookingRequest dto) {
        Booking b = new Booking();
        b.setFirstName(dto.getFirstName());
        b.setLastName(dto.getLastName());
        b.setPhone(dto.getPhone());
        b.setEmail(dto.getEmail());
        b.setPickupAddress(dto.getPickupAddress());
        b.setDropoffAddress(dto.getDropoffAddress());
        b.setTripType(dto.getTripType());
        b.setComment(dto.getComment());

        if (dto.getBookingDate() != null && dto.getBookingTime() != null) {
            b.setBookingDateTime(
                LocalDateTime.parse(dto.getBookingDate() + "T" + dto.getBookingTime())
            );
        }
        return bookingRepository.save(b);
    }

    public List<Booking> getAll() {
        return bookingRepository.findAllByOrderByCreatedAtDesc();
    }

    public Booking updateStatus(Long id, AdminResponseDto dto) {
        Booking b = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Non trouvé"));
        b.setStatus(Booking.BookingStatus.valueOf(dto.getStatus()));
        return bookingRepository.save(b);
    }
}