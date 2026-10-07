package com.stayease.model;

public class Room {
    private final int roomNumber;
    private final RoomType roomType;
    private boolean available;

    public Room(int roomNumber, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.available = true;
    }

    public int getRoomNumber() { return roomNumber; }
    public RoomType getRoomType() { return roomType; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return String.format(
            "Room %d | Type: %-7s | Price: ₹%.2f/night | Status: %s",
            roomNumber, roomType, roomType.getPricePerNight(),
            available ? "AVAILABLE" : "OCCUPIED"
        );
    }
}
