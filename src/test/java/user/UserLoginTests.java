package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.LoginUserResponseModel;
import praktikum.UserCreateModel;
import praktikum.UserLoginModel;

public class UserLoginTests {

    private final String email = "rat@ratmail.rat";
    private final String password = "1234";
    private final String name = "Oleg";

    private UserLoginModel userLoginModel;

    @Before
    public void setUp() {
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
        UserTestsHelper.compareResponseCode(response, 200);
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
        UserTestsHelper.compareResponseCode(response, 401);
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
        UserTestsHelper.compareResponseCode(response, 401);
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
