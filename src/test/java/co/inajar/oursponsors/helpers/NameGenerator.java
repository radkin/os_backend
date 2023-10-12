package co.inajar.oursponsors.helpers;

import java.util.Random;

public class NameGenerator {

    private static final String[] FIRST_NAMES = {
            "Alice", "Bob", "Charlie", "David", "Emma", "Frank", "Grace",
            "Hannah", "Isaac", "Jack", "Katherine", "Liam", "Mia", "Noah",
            "Olivia", "Peter", "Quinn", "Rachel", "Samuel", "Tina", "Ulysses",
            "Victoria", "William", "Xander", "Yvonne", "Zane"
    };

    private static final String[] LAST_NAMES = {
            "Smith", "Johnson", "Brown", "Davis", "Wilson", "Miller", "Moore",
            "Taylor", "Anderson", "Jackson", "Harris", "Martin", "White", "Lewis",
            "Clark", "Walker", "Young", "Hall", "Wright", "King"
    };

    public static void main(String[] args) {
        String randomName = generateRandomName();
        System.out.println("Generated Random Name: " + randomName);
    }

    public static String generateRandomName() {
        Random random = new Random();
        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        return firstName + " " + lastName;
    }
}

