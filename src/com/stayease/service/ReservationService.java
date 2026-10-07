package com.stayease.service;

import com.stayease.model.*;
import com.stayease.repository.ReservationRepository;
import com.stayease.util.IdGenerator;
import java.time.LocalDate;
import java.util.List;

public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final GuestService guestService;
    private final RoomService roomService;

    public ReservationService(ReservationRepository reservationRepository,
                              GuestService guestService,
                              RoomService roomService) {
        this.reservationRepository = reservationRepository;
        this.guestService = guestService;
        this.roomService = roomService;
    }

    public Reservation createReservation(String guestId, int roomNumber,
                                         LocalDate checkIn, LocalDate checkOut) {
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }

        Guest guest = guestService.findGuest(guestId);
        if (guest == null) throw new IllegalArgumentException("Guest not found.");

        Room room = roomService.findRoom(roomNumber);
        if (room == null) throw new IllegalArgumentException("Room not found.");
        if (!room.isAvailable()) throw new IllegalArgumentException("Room is currently occupied.");

        Reservation reservation = new Reservation(
            IdGenerator.generateReservationId(), guest, room, checkIn, checkOut
        );
        room.setAvailable(false);
        reservationRepository.save(reservation);
        return reservation;
    }

    public void cancelReservation(String reservationId) {
        Reservation reservation = findReservation(reservationId);
        if (reservation == null) throw new IllegalArgumentException("Reservation not found.");
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalArgumentException("Reservation is already cancelled.");
        }
        if (reservation.getStatus() == ReservationStatus.CHECKED_OUT) {
            throw new IllegalArgumentException("Checked-out reservation cannot be cancelled.");
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.getRoom().setAvailable(true);
    }

    public void checkIn(String reservationId) {
        Reservation reservation = findReservation(reservationId);
        if (reservation == null) throw new IllegalArgumentException("Reservation not found.");
        if (reservation.getStatus() != ReservationStatus.RESERVED) {
            throw new IllegalArgumentException("Guest cannot check in for this reservation.");
        }
        reservation.setStatus(ReservationStatus.CHECKED_IN);
    }

    public void checkOut(String reservationId) {
        Reservation reservation = findReservation(reservationId);
        if (reservation == null) throw new IllegalArgumentException("Reservation not found.");
        if (reservation.getStatus() != ReservationStatus.CHECKED_IN) {
            throw new IllegalArgumentException("Guest must check in before check-out.");
        }
        reservation.setStatus(ReservationStatus.CHECKED_OUT);
        reservation.getRoom().setAvailable(true);
    }

    public Reservation findReservation(String reservationId) {
        return reservationRepository.findById(reservationId);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
}
