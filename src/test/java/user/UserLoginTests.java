package user;

import common.TestsHelper;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import praktikum.LoginUserResponseModel;
import praktikum.UserCreateModel;
import praktikum.UserLoginModel;

public class UserLoginTests {

    private final static String email = "rat@ratmail.rat";
    private final static String password = "1234";
    private final static String name = "Oleg";

    private static UserLoginModel userLoginModel;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

        UserCreateModel userCreateModel = new UserCreateModel(
                email,
                password,
                "Oleg"
        );
        userLoginModel = new UserLoginModel(
                email,
                password
        );
        UserTestsHelper.sendPostCreateUser(userCreateModel);
    }

    @Test
    @DisplayName("Check successful login of user")
    public void checkSuccessfulLogin() {
        // Проверка логина пользователя
        Response response = UserTestsHelper.sendPostLoginCourier(userLoginModel);
        TestsHelper.compareResponseCode(response, 200);
    }

    @Test
    @DisplayName("Check login without login field")
    public void checkLoginWithoutLoginField() {
        UserLoginModel model = new UserLoginModel(
                "other.mail@ratmail.rat",
                password
        );

        // Проверка логина пользователя с неправильным email
        Response response = UserTestsHelper.sendPostLoginCourier(model);
        TestsHelper.compareResponseCode(response, 401);
    }

    @Test
    @DisplayName("Check login without password field")
    public void checkLoginWithoutPasswordField() {
        UserLoginModel model = new UserLoginModel(
                email,
                "1111"
        );

        // Проверка логина пользователя с неправильным паролем
        Response response = UserTestsHelper.sendPostLoginCourier(model);
        TestsHelper.compareResponseCode(response, 401);
    }

    @After
    public void clean() {
        // Возвращение тестового окружения к исходному виду
        UserLoginModel userLoginModel = new UserLoginModel(
                email,
                password
        );
        Response loginResponse = UserTestsHelper.sendPostLoginCourier(userLoginModel);

        if (loginResponse.statusCode() != 200) return;

        String token = loginResponse.body().as(LoginUserResponseModel.class).getAccessToken();
        if (token != null) {
            UserTestsHelper.sendDeleteCourier(token);
        }
    }
}
