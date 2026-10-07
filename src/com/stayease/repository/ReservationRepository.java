package com.stayease.repository;

import com.stayease.model.Reservation;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepository {
    private final List<Reservation> reservations = new ArrayList<>();

    public void save(Reservation reservation) { reservations.add(reservation); }

    public Reservation findById(String reservationId) {
        for (Reservation reservation : reservations) {
            if (reservation.getReservationId().equalsIgnoreCase(reservationId)) {
                return reservation;
            }
        }
        return null;
    }

    public List<Reservation> findAll() { return reservations; }
}
