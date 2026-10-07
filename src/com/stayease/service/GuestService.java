package com.stayease.service;

import com.stayease.model.Guest;
import com.stayease.repository.GuestRepository;
import com.stayease.util.IdGenerator;
import java.util.List;

public class GuestService {
    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public Guest registerGuest(String name, String phone, String email) {
        Guest guest = new Guest(IdGenerator.generateGuestId(), name, phone, email);
        guestRepository.save(guest);
        return guest;
    }

    public Guest findGuest(String guestId) { return guestRepository.findById(guestId); }
    public List<Guest> getAllGuests() { return guestRepository.findAll(); }
}
