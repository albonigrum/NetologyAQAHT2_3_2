package ru.netology.Info;

import com.github.javafaker.Faker;

import java.util.Locale;

public class UserInfo {
    public final String login;
    public final String password;

    private UserInfo(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static UserInfo getRandomUserInfo() {
        Faker faker = new Faker(new Locale("en"));
        return new UserInfo(
                faker.name().username(),
                faker.internet().password()
        );
    }
}
