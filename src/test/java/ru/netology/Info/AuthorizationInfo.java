package ru.netology.Info;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class AuthorizationInfo {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static final Faker faker = new Faker(new Locale("en"));

    public static final String ACTIVE_STATUS = "active";
    public static final String BLOCKED_STATUS = "blocked";
    public static final String NOT_EXIST_STATUS = "not exist";

    public final String login;
    public final String password;
    public final String status;

    private static String generateLogin() {
        return faker.name().username();
    }

    private static String generatePassword() {
        return faker.internet().password();
    }

    private static void updateUser(AuthorizationInfo info) {
        if (!info.status.equals(NOT_EXIST_STATUS))
            given()
                    .spec(requestSpec)
                    .body(info)
            .when()
                    .post("/api/system/users")
            .then()
                    .statusCode(200);
    }

    private AuthorizationInfo(String status) {
        this.login = generateLogin();
        this.password = generatePassword();

        if (!status.equals(ACTIVE_STATUS) && !status.equals(BLOCKED_STATUS) && !status.equals(NOT_EXIST_STATUS))
            throw new IllegalArgumentException(
                    "Status must be '" + ACTIVE_STATUS + "' or '" + BLOCKED_STATUS + "' or '" + NOT_EXIST_STATUS + "'");
        this.status = status;
    }

    private AuthorizationInfo(String login, String password, String status) {
        this.login = login;
        this.password = password;

        if (!status.equals(ACTIVE_STATUS) && !status.equals(BLOCKED_STATUS) && !status.equals(NOT_EXIST_STATUS))
            throw new IllegalArgumentException(
                    "Status must be '" + ACTIVE_STATUS + "' or '" + BLOCKED_STATUS + "' or '" + NOT_EXIST_STATUS + "'");
        this.status = status;
    }

    private static AuthorizationInfo getAuthorizationInfo(String status) {
        AuthorizationInfo info = new AuthorizationInfo(status);
        updateUser(info);
        return info;
    }

    private static AuthorizationInfo getAuthorizationInfo(String login, String password, String status) {
        AuthorizationInfo info = new AuthorizationInfo(login, password, status);
        updateUser(info);
        return info;
    }

    public static AuthorizationInfo getActiveAuthorizationInfo() {
        return getAuthorizationInfo(ACTIVE_STATUS);
    }

    public static AuthorizationInfo getBlockedAuthorizationInfo() {
        return getAuthorizationInfo(BLOCKED_STATUS);
    }

    public static AuthorizationInfo getNotExistAuthorizationInfo() {
        return getAuthorizationInfo(NOT_EXIST_STATUS);
    }

    public static AuthorizationInfo getNotExistAuthorizationInfoWithLogin(String login) {
        return getAuthorizationInfo(login, generatePassword(), NOT_EXIST_STATUS);
    }

    public static AuthorizationInfo getNotExistAuthorizationInfoWithPassword(String password) {
        return getAuthorizationInfo(generateLogin(), password, NOT_EXIST_STATUS);
    }
}
