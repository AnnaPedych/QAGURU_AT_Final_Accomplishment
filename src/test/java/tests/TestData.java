package tests;

import com.github.javafaker.Faker;

public class TestData {
    static Faker faker = new Faker();

    public static int voteOption = faker.number().numberBetween(0, 3);
}
