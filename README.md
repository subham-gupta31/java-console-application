# StayEase - Smart Hotel Reservation Management System

A console-based hotel reservation management system built with Core Java and OOP.

## Features

- Guest registration
- View guests
- View all rooms
- View available rooms
- Create reservations
- Search reservations
- Cancel reservations
- Guest check-in
- Guest check-out
- Automatic bill calculation
- Input validation
- Layered architecture

## Architecture

- model: Domain classes and enums
- repository: In-memory data storage
- service: Business logic
- ui: Console interface
- util: Reusable utilities

## Requirements

- Java 17 or later
- VS Code with Extension Pack for Java

## Run

From the project root:

```bash
javac -d out src/com/stayease/**/*.java src/com/stayease/*.java
java -cp out com.stayease.Main
```

In VS Code, open the `StayEase` folder and run `src/com/stayease/Main.java`.

## Note

This version stores data in memory. The next upgrade can add JDBC + MySQL persistence, login/roles, payments, reports, and a more advanced reservation/date-availability system.
