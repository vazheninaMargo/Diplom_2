package user;

import api.client.LoginUserResponseModel;
import api.client.UserCreateModel;
import api.client.UserLoginModel;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.ApiClient;

public class UserCreateTests {

    private final String email = "rat8@ratmail.rat";
    private final String password = "123456";
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
        Response createResponse = ApiClient.User.sendPostCreateUser(userCreateModel);
        createResponse.then().statusCode(200);
        Assert.assertEquals(createResponse.then().extract().body().path("success"), true);
        Assert.assertEquals(createResponse.then().extract().body().path("user.name"), name);
        Assert.assertEquals(createResponse.then().extract().body().path("user.email"), email);
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
        ApiClient.User.sendPostCreateUser(userCreateModel);
        // Создание второго пользователя
        Response createResponse = ApiClient.User.sendPostCreateUser(userCreateModel);
        createResponse.then().statusCode(403);
        Assert.assertEquals(createResponse.then().extract().body().path("success"), false);
        Assert.assertEquals(createResponse.then().extract().body().path("message"), "User already exists");
    }

    @Test
    @DisplayName("Check creating of user without email")
    public void checkCreatingOfUserWithoutLogin() {
        Response withoutEmail = ApiClient.User.sendPostCreateUser(new UserCreateModel(
                null,
                password,
                name
        ));
        withoutEmail.then().statusCode(403);
        Assert.assertEquals(withoutEmail.then().extract().body().path("success"), false);
        Assert.assertEquals(withoutEmail.then().extract().body().path("message"), "Email, password and name are required fields");
    }

    @After
    public void clean() {
        // Возвращение тестового окружения к исходному виду
        UserLoginModel userLoginModel = new UserLoginModel(
                email,
                password
        );
        Response loginResponse = ApiClient.User.sendPostLoginUser(userLoginModel);

        if (loginResponse.statusCode() != 200) return;

        String token = loginResponse.body().as(LoginUserResponseModel.class).getAccessToken();
        if (token != null) {
            ApiClient.User.sendDeleteCourier(token);
        }
    }
}
