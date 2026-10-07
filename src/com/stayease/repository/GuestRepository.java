package com.stayease.repository;

import com.stayease.model.Guest;
import java.util.ArrayList;
import java.util.List;

public class GuestRepository {
    private final List<Guest> guests = new ArrayList<>();

    public void save(Guest guest) { guests.add(guest); }

    public Guest findById(String guestId) {
        for (Guest guest : guests) {
            if (guest.getGuestId().equalsIgnoreCase(guestId)) return guest;
        }
        return null;
    }

    public List<Guest> findAll() { return guests; }
}
