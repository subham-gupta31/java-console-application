package com.stayease.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation {
    private final String reservationId;
    private final Guest guest;
    private final Room room;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private ReservationStatus status;

    public Reservation(String reservationId, Guest guest, Room room,
                       LocalDate checkInDate, LocalDate checkOutDate) {
        this.reservationId = reservationId;
        this.guest = guest;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = ReservationStatus.RESERVED;
    }

    public String getReservationId() { return reservationId; }
    public Guest getGuest() { return guest; }
    public Room getRoom() { return room; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }

    public long getNumberOfNights() {
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    public double calculateBill() {
        return getNumberOfNights() * room.getRoomType().getPricePerNight();
    }

    @Override
    public String toString() {
        return "\nReservation ID : " + reservationId +
               "\nGuest          : " + guest.getName() +
               "\nRoom           : " + room.getRoomNumber() +
               "\nRoom Type      : " + room.getRoomType() +
               "\nCheck-in       : " + checkInDate +
               "\nCheck-out      : " + checkOutDate +
               "\nNights         : " + getNumberOfNights() +
               "\nAmount         : ₹" + String.format("%.2f", calculateBill()) +
               "\nStatus         : " + status;
    }
}
