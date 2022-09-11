package user;

import api.client.LoginUserResponseModel;
import api.client.UserCreateModel;
import api.client.UserLoginModel;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.ApiClient;

public class UserLoginTests {

    private final static String email = "rat8@ratmail.rat";
    private final static String password = "123456";
    private final static String name = "Oleg";

    private static UserLoginModel userLoginModel;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

        UserCreateModel userCreateModel = new UserCreateModel(
                email,
                password,
                name
        );
        userLoginModel = new UserLoginModel(
                email,
                password
        );
        ApiClient.User.sendPostCreateUser(userCreateModel);
    }

    @Test
    @DisplayName("Check successful login of user")
    public void checkSuccessfulLogin() {
        // Проверка логина пользователя
        Response response = ApiClient.User.sendPostLoginUser(userLoginModel);
        response.then().statusCode(200);
        Assert.assertEquals(response.body().path("success"), true);
    }

    @Test
    @DisplayName("Check login without login field")
    public void checkLoginWithNonExistentLogin() {
        UserLoginModel model = new UserLoginModel(
                "other.mail@ratmail.rat",
                password
        );

        // Проверка логина пользователя с неправильным email
        Response response = ApiClient.User.sendPostLoginUser(model);
        response.then().statusCode(401);
        Assert.assertEquals(response.body().path("success"), false);
        Assert.assertEquals(response.body().path("message"), "email or password are incorrect");
    }

    @Test
    @DisplayName("Check login without password field")
    public void checkLoginWithIncorrectPassword() {
        UserLoginModel model = new UserLoginModel(
                email,
                "1111"
        );

        // Проверка логина пользователя с неправильным паролем
        Response response = ApiClient.User.sendPostLoginUser(model);
        response.then().statusCode(401);
        Assert.assertEquals(response.body().path("success"), false);
        Assert.assertEquals(response.body().path("message"), "email or password are incorrect");
    }

    @AfterClass
    public static void clean() {
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
