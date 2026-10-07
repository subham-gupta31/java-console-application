package com.stayease.repository;

import com.stayease.model.Room;
import com.stayease.model.RoomType;
import java.util.ArrayList;
import java.util.List;

public class RoomRepository {
    private final List<Room> rooms = new ArrayList<>();

    public RoomRepository() {
        rooms.add(new Room(101, RoomType.SINGLE));
        rooms.add(new Room(102, RoomType.SINGLE));
        rooms.add(new Room(103, RoomType.SINGLE));
        rooms.add(new Room(201, RoomType.DOUBLE));
        rooms.add(new Room(202, RoomType.DOUBLE));
        rooms.add(new Room(203, RoomType.DOUBLE));
        rooms.add(new Room(301, RoomType.DELUXE));
        rooms.add(new Room(302, RoomType.DELUXE));
        rooms.add(new Room(401, RoomType.SUITE));
        rooms.add(new Room(402, RoomType.SUITE));
    }

    public List<Room> findAll() { return rooms; }

    public Room findByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) return room;
        }
        return null;
    }

    public List<Room> findAvailableRooms() {
        List<Room> result = new ArrayList<>();
        for (Room room : rooms) {
            if (room.isAvailable()) result.add(room);
        }
        return result;
    }

    public List<Room> findAvailableRoomsByType(RoomType type) {
        List<Room> result = new ArrayList<>();
        for (Room room : rooms) {
            if (room.isAvailable() && room.getRoomType() == type) result.add(room);
        }
        return result;
    }
}
