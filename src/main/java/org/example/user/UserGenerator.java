package org.example.user;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;


public class UserGenerator {
    public static User getDefaultUser() {
        return new User("vasya@test.com", "password", "Вася");
    }

    @Step("Автогенерация уникального пользователя")
    public static User getUniqueUser() {
        Faker faker = new Faker();

        String randomName = faker.name().fullName();
        String randomPassword = RandomStringUtils.randomNumeric(8);
        String randomEmail = RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@test.com";
        return new User(randomEmail, randomPassword, randomName);
    }

    @Step("Генерация пользователя: {email}, {password}, {name}")
    public static User getManuallyGeneratedUser(String email, String password, String name) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        return user;
    }

}

