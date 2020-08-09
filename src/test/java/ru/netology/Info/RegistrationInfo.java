package ru.netology.Info;

import com.github.javafaker.Faker;

import java.util.Locale;

public class RegistrationInfo {
    public final String login;
    public final String password;
    public final String status;

    private RegistrationInfo(String login, String password, String status) {
        this.login = login;
        this.password = password;
        this.status = status;
    }

    public static RegistrationInfo getActiveRandomRegistrationInfo(UserInfo userInfo) {
        return new RegistrationInfo(
                userInfo.login,
                userInfo.password,
                "active"
        );
    }

    public static RegistrationInfo getBlockedRandomRegistrationInfo(UserInfo userInfo) {
        return new RegistrationInfo(
                userInfo.login,
                userInfo.password,
                "blocked"
        );
    }
}
