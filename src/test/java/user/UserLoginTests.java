package user;

import api.client.login.LoginResponseModel;
import api.client.registration.RegistrationModel;
import api.client.login.LoginModel;
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

    private static LoginModel userLoginModel;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

        RegistrationModel userCreateModel = new RegistrationModel(
                email,
                password,
                name
        );
        userLoginModel = new LoginModel(
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
        LoginModel model = new LoginModel(
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
        LoginModel model = new LoginModel(
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
        LoginModel userLoginModel = new LoginModel(
                email,
                password
        );
        Response loginResponse = ApiClient.User.sendPostLoginUser(userLoginModel);

        if (loginResponse.statusCode() != 200) return;

        String token = loginResponse.body().as(LoginResponseModel.class).getAccessToken();
        if (token != null) {
            ApiClient.User.sendDeleteCourier(token);
        }
    }
}
