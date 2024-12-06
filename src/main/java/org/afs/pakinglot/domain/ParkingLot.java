package org.afs.pakinglot.domain;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.afs.pakinglot.domain.exception.NoAvailablePositionException;
import org.afs.pakinglot.domain.exception.UnrecognizedTicketException;

public class ParkingLot {
    private int id;
    private String name;
    private final Map<Ticket, Car> tickets = new HashMap<>();

    private static final int DEFAULT_CAPACITY = 10;
    private final int capacity;

    public ParkingLot() {
        this(DEFAULT_CAPACITY);
    }

    public ParkingLot(int capacity) {
        this.capacity = capacity;
    }

    public ParkingLot(int id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getAvailableCapacity() {
        return capacity - tickets.size();
    }

    public Ticket park(Car car) {
        if (isFull()) {
            throw new NoAvailablePositionException();
        }

        Ticket ticket = new Ticket(car.plateNumber(),  getPositionToPark(), this.id);
        tickets.put(ticket, car);
        return ticket;
    }

    public boolean isFull() {
        return capacity == tickets.size();
    }

    public Car fetch(Ticket ticket) {
        if (!tickets.containsKey(ticket)) {
            throw new UnrecognizedTicketException();
        }

        return tickets.remove(ticket);
    }

    public boolean contains(Ticket ticket) {
        return tickets.containsKey(ticket);
    }

    public double getAvailablePositionRate() {
        return getAvailableCapacity() / (double) capacity;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<Ticket> getTickets() {
        return tickets.keySet().stream().toList();
    }

    public int getPositionToPark() {
        // Context:  I have some tickets, each ticket has a position
        // The position is the order of the car parked in the parking lot, for example parking lot has 9 positions
        // Given only a ticket position is 5
        // Then should return the position 1
        for (int postionIdex = 1; postionIdex <= capacity; postionIdex++) {
            int targetPosition = postionIdex;
            if (tickets.keySet().stream().noneMatch(ticket -> ticket.position() == targetPosition)) {
                return targetPosition;
            }
        }
        throw new NoAvailablePositionException();
    }
}
