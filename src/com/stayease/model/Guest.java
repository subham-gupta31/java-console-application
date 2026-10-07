package com.stayease.model;

public class Guest {
    private final String guestId;
    private final String name;
    private final String phone;
    private final String email;

    public Guest(String guestId, String name, String phone, String email) {
        this.guestId = guestId;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getGuestId() { return guestId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return String.format(
            "Guest ID: %s | Name: %s | Phone: %s | Email: %s",
            guestId, name, phone, email
        );
    }
}
