package com.stayease.util;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    private static final AtomicInteger guestCounter = new AtomicInteger(1001);
    private static final AtomicInteger reservationCounter = new AtomicInteger(5001);

    public static String generateGuestId() {
        return "GST" + guestCounter.getAndIncrement();
    }

    public static String generateReservationId() {
        return "RES" + reservationCounter.getAndIncrement();
    }
}
