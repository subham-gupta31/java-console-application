package com.stayease.service;

import com.stayease.model.Room;
import com.stayease.model.RoomType;
import com.stayease.repository.RoomRepository;
import java.util.List;

public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() { return roomRepository.findAll(); }
    public List<Room> getAvailableRooms() { return roomRepository.findAvailableRooms(); }
    public List<Room> getAvailableRoomsByType(RoomType type) {
        return roomRepository.findAvailableRoomsByType(type);
    }
    public Room findRoom(int roomNumber) { return roomRepository.findByNumber(roomNumber); }
}
