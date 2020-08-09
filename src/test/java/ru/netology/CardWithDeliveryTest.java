package ru.netology;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.netology.Info.RegistrationInfo;
import ru.netology.Info.UserInfo;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static ru.netology.Info.RegistrationInfo.*;
import static ru.netology.Info.UserInfo.getRandomUserInfo;

public class CardWithDeliveryTest {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    void updateUser(RegistrationInfo registrationInfo) {
        given()
                .spec(requestSpec)
                .body(registrationInfo)
        .when()
                .post("/api/system/users")
        .then()
                .statusCode(200);
    }

    void blockUser(UserInfo userInfo) {
        updateUser(getBlockedRegistrationInfoByUserInfo(userInfo));
    }

    void createUser(UserInfo userInfo) {
        updateUser(getActiveRegistrationInfoByUserInfo(userInfo));
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Nested
    class HappyPathTests {
        @Test
        void shouldLoginIfActive() {
            UserInfo userInfo = getRandomUserInfo();
            try {
                createUser(userInfo);
                $("[data-test-id=login] input").sendKeys(userInfo.login);
                $("[data-test-id=password] input").sendKeys(userInfo.password);

                $("button[data-test-id=action-login]").click();
            } finally {
                blockUser(userInfo);
            }
        }

        @Test
        void shouldNotLoginIfBlocked() {
            UserInfo userInfo = getRandomUserInfo();
            blockUser(userInfo);

            $("[data-test-id=login] input").sendKeys(userInfo.login);
            $("[data-test-id=password] input").sendKeys(userInfo.password);

            $("button[data-test-id=action-login]").click();

            $("[data-test-id=error-notification]").shouldHave(text("Ошибка! Пользователь заблокирован"));
        }

        @Test
        void shouldNotLoginIfNotExist() {
            UserInfo userInfo = getRandomUserInfo();

            $("[data-test-id=login] input").sendKeys(userInfo.login);
            $("[data-test-id=password] input").sendKeys(userInfo.password);

            $("button[data-test-id=action-login]").click();

            $("[data-test-id=error-notification]").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
        }
    }

    @Nested
    class SadPathTests {
        @Test
        void shouldNotLoginIfIncorrectPassword() {
            UserInfo userInfo = getRandomUserInfo();
            try {
                createUser(userInfo);
                $("[data-test-id=login] input").sendKeys(userInfo.login);
                $("[data-test-id=password] input").sendKeys("prefix" + userInfo.password);

                $("button[data-test-id=action-login]").click();
                $("[data-test-id=error-notification]").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
            } finally {
                blockUser(userInfo);
            }
        }
        @Test
        void shouldNotLoginIfIncorrectUser() {
            UserInfo userInfo = getRandomUserInfo();
            try {
                createUser(userInfo);
                $("[data-test-id=login] input").sendKeys("prefix" + userInfo.login);
                $("[data-test-id=password] input").sendKeys(userInfo.password);

                $("button[data-test-id=action-login]").click();

                $("[data-test-id=error-notification]").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
            } finally {
                blockUser(userInfo);
            }
        }
    }

}
