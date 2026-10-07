package com.stayease.model;

public enum RoomType {
    SINGLE(1500),
    DOUBLE(2500),
    DELUXE(3500),
    SUITE(5000);

    private final double pricePerNight;

    RoomType(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }
}
