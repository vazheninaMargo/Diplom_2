package user;

import common.TestsHelper;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import praktikum.LoginUserResponseModel;
import praktikum.UserCreateModel;
import praktikum.UserLoginModel;

public class UserCreateTests {

    private final String email = "Rat@ratmail.rat";
    private final String password = "1234";
    private final String name = "Oleg";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test
    @DisplayName("Check successful creating of user")
    public void checkSuccessfulCreatingOfUser() {
        UserCreateModel userCreateModel = new UserCreateModel(
                email,
                password,
                name
        );

        // Создание пользователя
        Response createResponse = UserTestsHelper.sendPostCreateUser(userCreateModel);
        TestsHelper.compareResponseCode(createResponse, 200);
        Assert.assertEquals(createResponse.then().extract().body().path("success"), true);
    }

    @Test
    @DisplayName("Check duplicate of user creating")
    public void checkDuplicateCreatingOfUser() {
        UserCreateModel userCreateModel = new UserCreateModel(
                email,
                password,
                name
        );

        // Создание первого пользователя
        UserTestsHelper.sendPostCreateUser(userCreateModel);
        // Создание второго пользователя
        Response createResponse = UserTestsHelper.sendPostCreateUser(userCreateModel);
        TestsHelper.compareResponseCode(createResponse, 403);
    }

    @Test
    @DisplayName("Check creating of user without email")
    public void checkCreatingOfUserWithoutLogin() {
        Response withoutEmail = UserTestsHelper.sendPostCreateUser(new UserCreateModel(
                null,
                password,
                name
        ));
        TestsHelper.compareResponseCode(withoutEmail, 403);
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
