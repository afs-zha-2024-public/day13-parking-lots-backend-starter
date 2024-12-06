package org.afs.pakinglot.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.afs.pakinglot.domain.exception.NoAvailablePositionException;
import org.afs.pakinglot.domain.exception.UnrecognizedTicketException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ParkingLotTest {
    @Test
    void should_return_ticket_when_park_given_a_parking_lot_and_a_car() {
        // Given
        ParkingLot parkingLot = new ParkingLot();
        Car car = new Car(CarPlateGenerator.generatePlate());
        // When
        Ticket ticket = parkingLot.park(car);
        // Then
        assertNotNull(ticket);
    }

    @Test
    void should_return_car_when_fetch_given_a_parking_lot_and_a_ticket() {
        // Given
        ParkingLot parkingLot = new ParkingLot();
        Car car = new Car(CarPlateGenerator.generatePlate());
        Ticket ticket = parkingLot.park(car);
        // When
        Car fetchedCar = parkingLot.fetch(ticket);
        // Then
        assertEquals(car, fetchedCar);
    }

    @Test
    void should_return_correct_car_when_fetch_twice_given_a_parking_lot_and_two_tickets() {
        // Given
        ParkingLot parkingLot = new ParkingLot();
        Car car1 = new Car(CarPlateGenerator.generatePlate());
        Car car2 = new Car(CarPlateGenerator.generatePlate());
        Ticket ticket1 = parkingLot.park(car1);
        Ticket ticket2 = parkingLot.park(car2);
        // When
        Car fetchedCar1 = parkingLot.fetch(ticket1);
        Car fetchedCar2 = parkingLot.fetch(ticket2);
        // Then
        assertEquals(car1, fetchedCar1);
        assertEquals(car2, fetchedCar2);
    }

    @Test
    void should_return_nothing_with_error_message_when_park_given_a_parking_lot_and_a_car_and_parking_lot_is_full() {
        // Given
        ParkingLot parkingLot = new ParkingLot();
        for (int i = 0; i < parkingLot.getCapacity(); i++) {
            parkingLot.park(new Car(CarPlateGenerator.generatePlate()));
        }
        Car car = new Car(CarPlateGenerator.generatePlate());
        // When
        // Then
        NoAvailablePositionException exception =
            assertThrows(NoAvailablePositionException.class, () -> parkingLot.park(car));
        assertEquals("No available position.", exception.getMessage());
    }

    @Test
    void should_return_nothing_with_error_message_when_fetch_given_a_parking_lot_and_an_unrecognized_ticket() {
        // Given
        ParkingLot parkingLot = new ParkingLot();
        Ticket unrecognizedTicket = new Ticket(CarPlateGenerator.generatePlate(), 1, 1);
        // When
        // Then
        UnrecognizedTicketException exception =
            assertThrows(UnrecognizedTicketException.class, () -> parkingLot.fetch(unrecognizedTicket));
        assertEquals("Unrecognized parking ticket.", exception.getMessage());
    }

    @Test
    void should_return_nothing_with_error_message_when_fetch_given_a_parking_lot_and_a_used_ticket() {
        // Given
        ParkingLot parkingLot = new ParkingLot();
        Car car = new Car(CarPlateGenerator.generatePlate());
        Ticket ticket = parkingLot.park(car);
        parkingLot.fetch(ticket);
        // When
        // Then
        UnrecognizedTicketException exception =
            assertThrows(UnrecognizedTicketException.class, () -> parkingLot.fetch(ticket));
        assertEquals("Unrecognized parking ticket.", exception.getMessage());
    }

    @Test
    void should_return_ticks_list_when_getTickets_given_a_some_parked_cars() {
        // Given
        ParkingLot parkingLot = new ParkingLot();
        Car car1 = new Car(CarPlateGenerator.generatePlate());
        Car car2 = new Car(CarPlateGenerator.generatePlate());
        Ticket ticket1 = parkingLot.park(car1);
        Ticket ticket2 = parkingLot.park(car2);
        List<Ticket> expectedTickets = List.of(ticket1, ticket2);
        // When
        List<Ticket> tickets = parkingLot.getTickets();
        // Then
        assertNotNull(tickets);
        assertTrue(expectedTickets.containsAll(tickets));
    }

    @Nested
    @DisplayName("Parking Tickets Position Tests")
    class ParkingTests {
        // Context:
        // Description : I need a function to get the position of the car in the parking lot
        // Input: is the tickets of the parking lot
        // Output: is the position of the car should park
        // Call this function is "getPositionToPark"\

        // Case 1:
        // Given an empty parking lot without tickets, capacity is 9
        // When getPositionToPark Then return the position number is 1
        @Test
        void should_return_position_1_when_getPositionToPark_given_an_empty_parking_lot() {
            // Given
            ParkingLot parkingLot = new ParkingLot();
            // When
            int position = parkingLot.getPositionToPark();
            // Then
            assertEquals(1, position);
        }

        // Case 2:
        // Given a NOT empty parking lot, capacity is 9
        // Given a ticket, the ticket position is 1
        // When getPositionToPark Then return the position number is 2
        @Test
        void should_return_position_2_when_getPositionToPark_given_a_parking_lot_with_a_ticket() {
            // Given
            ParkingLot parkingLot = new ParkingLot();
            parkingLot.park(new Car(CarPlateGenerator.generatePlate()));
            // When
            int position = parkingLot.getPositionToPark();
            // Then
            assertEquals(2, position);
        }

        // Case 3:
        // Given a NOT empty parking lot, capacity is 9
        // Given a ticket, the position is 5
        // Given all other positions are empty
        // When getPositionToPark Then return ticket with position 1
        @Test
        void should_return_position_1_when_getPositionToPark_given_a_parking_lot_with_a_ticket_in_position_5() {
            // Given
            ParkingLot parkingLot = new ParkingLot();
            Ticket ticketCar1 = parkingLot.park(new Car(CarPlateGenerator.generatePlate()));
            Ticket ticketCar2 = parkingLot.park(new Car(CarPlateGenerator.generatePlate()));
            Ticket ticketCar3 = parkingLot.park(new Car(CarPlateGenerator.generatePlate()));
            Ticket ticketCar4 = parkingLot.park(new Car(CarPlateGenerator.generatePlate()));
            parkingLot.park(new Car(CarPlateGenerator.generatePlate()));

            parkingLot.fetch(ticketCar1);
            parkingLot.fetch(ticketCar2);
            parkingLot.fetch(ticketCar3);
            parkingLot.fetch(ticketCar4);
            // When
            int position = parkingLot.getPositionToPark();

            // Then
            assertEquals(1, position);
        }

        // Case 4:
        // Given a NOT empty parking lot, capacity is 9
        // Given some tickets, the position is 1,3,5,7,9
        // Given all other positions are empty
        // When getPositionToPark Then return ticket with position 2
        @Test
        void should_return_position_2_when_getPositionToPark_given_a_parking_lot_with_tickets_in_position_1_3_5_7_9() {
            // Given
            ParkingLot parkingLot = new ParkingLot();
            parkingLot.park(new Car(CarPlateGenerator.generatePlate()));
            Ticket ticketCar2 = parkingLot.park(new Car(CarPlateGenerator.generatePlate()));
            parkingLot.park(new Car(CarPlateGenerator.generatePlate()));
            Ticket ticketCar4 = parkingLot.park(new Car(CarPlateGenerator.generatePlate()));
            Ticket ticketCar5 = parkingLot.park(new Car(CarPlateGenerator.generatePlate()));
            Ticket ticketCar6 = parkingLot.park(new Car(CarPlateGenerator.generatePlate()));
            parkingLot.park(new Car(CarPlateGenerator.generatePlate()));
            Ticket ticketCar8 = parkingLot.park(new Car(CarPlateGenerator.generatePlate()));
            parkingLot.park(new Car(CarPlateGenerator.generatePlate()));

            parkingLot.fetch(ticketCar2);
            parkingLot.fetch(ticketCar4);
            parkingLot.fetch(ticketCar5);
            parkingLot.fetch(ticketCar6);
            parkingLot.fetch(ticketCar8);
            // When
            int position = parkingLot.getPositionToPark();

            // Then
            assertEquals(2, position);
        }
    }
}
