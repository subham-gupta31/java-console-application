package com.stayease.ui;

import com.stayease.model.Guest;
import com.stayease.model.Reservation;
import com.stayease.model.Room;
import com.stayease.service.GuestService;
import com.stayease.service.ReservationService;
import com.stayease.service.RoomService;
import com.stayease.util.InputUtil;
import java.time.LocalDate;
import java.util.List;

public class ConsoleUI {
    private final GuestService guestService;
    private final RoomService roomService;
    private final ReservationService reservationService;

    public ConsoleUI(GuestService guestService, RoomService roomService,
                     ReservationService reservationService) {
        this.guestService = guestService;
        this.roomService = roomService;
        this.reservationService = reservationService;
    }

    public void start() {
        showWelcome();
        boolean running = true;

        while (running) {
            showMenu();
            int choice = InputUtil.readInt("Enter your choice: ");
            System.out.println();

            try {
                switch (choice) {
                    case 1 -> registerGuest();
                    case 2 -> showAllGuests();
                    case 3 -> showAllRooms();
                    case 4 -> showAvailableRooms();
                    case 5 -> createReservation();
                    case 6 -> showAllReservations();
                    case 7 -> searchReservation();
                    case 8 -> cancelReservation();
                    case 9 -> checkIn();
                    case 10 -> checkOut();
                    case 11 -> {
                        running = false;
                        System.out.println("Thank you for using StayEase!");
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private void showWelcome() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("                    STAYEASE HOTEL");
        System.out.println("          Smart Hotel Reservation System");
        System.out.println("============================================================");
    }

    private void showMenu() {
        System.out.println("\n-------------------- MAIN MENU --------------------");
        System.out.println("1. Register Guest");
        System.out.println("2. View All Guests");
        System.out.println("3. View All Rooms");
        System.out.println("4. View Available Rooms");
        System.out.println("5. Create Reservation");
        System.out.println("6. View All Reservations");
        System.out.println("7. Search Reservation");
        System.out.println("8. Cancel Reservation");
        System.out.println("9. Check-In Guest");
        System.out.println("10. Check-Out Guest");
        System.out.println("11. Exit");
        System.out.println("---------------------------------------------------");
    }

    private void registerGuest() {
        System.out.println("------------- REGISTER GUEST -------------");
        String name = InputUtil.readString("Enter guest name: ");
        String phone = InputUtil.readPhone("Enter phone number: ");
        String email = InputUtil.readEmail("Enter email: ");
        Guest guest = guestService.registerGuest(name, phone, email);
        System.out.println("\nGuest registered successfully!");
        System.out.println(guest);
    }

    private void showAllGuests() {
        System.out.println("------------- ALL GUESTS -------------");
        List<Guest> guests = guestService.getAllGuests();
        if (guests.isEmpty()) {
            System.out.println("No guests registered.");
            return;
        }
        guests.forEach(System.out::println);
    }

    private void showAllRooms() {
        System.out.println("------------- ALL ROOMS -------------");
        roomService.getAllRooms().forEach(System.out::println);
    }

    private void showAvailableRooms() {
        System.out.println("------------- AVAILABLE ROOMS -------------");
        List<Room> rooms = roomService.getAvailableRooms();
        if (rooms.isEmpty()) {
            System.out.println("No rooms are currently available.");
            return;
        }
        rooms.forEach(System.out::println);
    }

    private void createReservation() {
        System.out.println("------------- CREATE RESERVATION -------------");
        String guestId = InputUtil.readString("Enter Guest ID: ");

        if (guestService.findGuest(guestId) == null) {
            System.out.println("Guest not found. Please register first.");
            return;
        }

        System.out.println("\nAvailable Rooms:");
        roomService.getAvailableRooms().forEach(System.out::println);

        int roomNumber = InputUtil.readInt("\nEnter room number: ");
        LocalDate checkIn = InputUtil.readDate("Enter check-in date (YYYY-MM-DD): ");
        LocalDate checkOut = InputUtil.readDate("Enter check-out date (YYYY-MM-DD): ");

        Reservation reservation = reservationService.createReservation(
            guestId, roomNumber, checkIn, checkOut
        );

        System.out.println("\nReservation created successfully!");
        System.out.println(reservation);
    }

    private void showAllReservations() {
        System.out.println("------------- ALL RESERVATIONS -------------");
        List<Reservation> reservations = reservationService.getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }
        for (Reservation reservation : reservations) {
            System.out.println(reservation);
            System.out.println("---------------------------------------------");
        }
    }

    private void searchReservation() {
        String id = InputUtil.readString("Enter Reservation ID: ");
        Reservation reservation = reservationService.findReservation(id);
        System.out.println(reservation == null ? "Reservation not found." : reservation);
    }

    private void cancelReservation() {
        String id = InputUtil.readString("Enter Reservation ID: ");
        reservationService.cancelReservation(id);
        System.out.println("Reservation cancelled successfully.");
    }

    private void checkIn() {
        String id = InputUtil.readString("Enter Reservation ID: ");
        reservationService.checkIn(id);
        System.out.println("Guest checked in successfully.");
    }

    private void checkOut() {
        String id = InputUtil.readString("Enter Reservation ID: ");
        Reservation reservation = reservationService.findReservation(id);
        if (reservation == null) {
            System.out.println("Reservation not found.");
            return;
        }

        reservationService.checkOut(id);

        System.out.println("\nGuest checked out successfully.");
        System.out.println("================ FINAL BILL ================");
        System.out.println("Reservation ID : " + reservation.getReservationId());
        System.out.println("Guest          : " + reservation.getGuest().getName());
        System.out.println("Room           : " + reservation.getRoom().getRoomNumber());
        System.out.println("Nights         : " + reservation.getNumberOfNights());
        System.out.printf("Room Charge    : ₹%.2f%n", reservation.calculateBill());
        System.out.println("=============================================");
    }
}
