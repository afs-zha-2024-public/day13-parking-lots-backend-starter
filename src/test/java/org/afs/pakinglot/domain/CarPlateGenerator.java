package org.afs.pakinglot.domain;

import java.util.Random;

public class CarPlateGenerator {
    // Method to generate a random car plate
    public static String generatePlate() {
        Random random = new Random();

        // Generate two random uppercase letters
        char letter1 = (char) ('A' + random.nextInt(26));
        char letter2 = (char) ('A' + random.nextInt(26));

        // Generate four random digits
        int digits = random.nextInt(10000); // Random number between 0 and 9999

        return String.format("%c%c-%04d", letter1, letter2, digits);
    }

    public static void main(String[] args) {
        // Test the generatePlate method
        System.out.println(CarPlateGenerator.generatePlate());
    }
}