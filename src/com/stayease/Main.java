package com.stayease;

import com.stayease.repository.GuestRepository;
import com.stayease.repository.ReservationRepository;
import com.stayease.repository.RoomRepository;
import com.stayease.service.GuestService;
import com.stayease.service.ReservationService;
import com.stayease.service.RoomService;
import com.stayease.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        GuestRepository guestRepository = new GuestRepository();
        RoomRepository roomRepository = new RoomRepository();
        ReservationRepository reservationRepository = new ReservationRepository();

        GuestService guestService = new GuestService(guestRepository);
        RoomService roomService = new RoomService(roomRepository);
        ReservationService reservationService =
                new ReservationService(reservationRepository, guestService, roomService);

        new ConsoleUI(guestService, roomService, reservationService).start();
    }
}
