package ru.netology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.netology.Info.AuthorizationInfo;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.Info.AuthorizationInfo.*;

public class AuthorizationFormTest {
    private AuthorizationInfo authInfo;

    void inputFieldsFormAndClickLogin(AuthorizationInfo authInfo) {
        $("[data-test-id=login] input").sendKeys(authInfo.login);
        $("[data-test-id=password] input").sendKeys(authInfo.password);

        $("button[data-test-id=action-login]").click();
    }

    void inputFieldsFormAndClickLogin() {
        inputFieldsFormAndClickLogin(authInfo);
    }

    void checkErrorUserOrPasswordIncorrect() {
        $("[data-test-id=error-notification]").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    void checkErrorUserBlocked() {
        $("[data-test-id=error-notification]").shouldHave(text("Ошибка! Пользователь заблокирован"));
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Nested
    class HappyPathTests {
        @Test
        void shouldLoginIfActive() {
            authInfo = getActiveAuthorizationInfo();
            inputFieldsFormAndClickLogin();
        }

        @Test
        void shouldNotLoginIfBlocked() {
            authInfo = getBlockedAuthorizationInfo();

            inputFieldsFormAndClickLogin();

            checkErrorUserBlocked();
        }

        @Test
        void shouldNotLoginIfNotExist() {
            //Так как нет операции удаления этот тест может не проходить в случае генерации одинаковых данных
            authInfo = getNotExistAuthorizationInfo();

            inputFieldsFormAndClickLogin();

            checkErrorUserOrPasswordIncorrect();
        }
    }

    @Nested
    class SadPathTests {
        @Test
        void shouldNotLoginIfIncorrectPassword() {
            authInfo = getActiveAuthorizationInfo();

            inputFieldsFormAndClickLogin(getNotExistAuthorizationInfoWithLogin(authInfo.login));

            checkErrorUserOrPasswordIncorrect();
        }

        @Test
        void shouldNotLoginIfIncorrectUser() {
            authInfo = getActiveAuthorizationInfo();

            inputFieldsFormAndClickLogin(getNotExistAuthorizationInfoWithPassword(authInfo.password));

            checkErrorUserOrPasswordIncorrect();
        }
    }

}
