package user;

import api.client.login.LoginResponseModel;
import api.client.registration.RegistrationModel;
import api.models.UserModel;
import api.client.login.LoginModel;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.ApiClient;

public class UserChangeTests {

    private String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

        String email = "rat8@ratmail.rat";
        String password = "1234";
        String name = "Oleg";

        RegistrationModel userCreateModel = new RegistrationModel(
                email,
                password,
                name
        );
        LoginModel userLoginModel = new LoginModel(
                email,
                password
        );
        ApiClient.User.sendPostCreateUser(userCreateModel);
        Response loginResponse = ApiClient.User.sendPostLoginUser(userLoginModel);
        token = loginResponse.body().as(LoginResponseModel.class).getAccessToken();
    }

    @Test
    @DisplayName("Check successful updating of users name")
    public void checkSuccessfulUpdatingOfNameWithAuthorization() {

        // Получение данных пользователя
        Response userInfoResponse = ApiClient.User.sendGetUserInfo(token);
        UserModel userInfo = userInfoResponse.as(UserModel.class);

        // Изменение данных пользователя
        userInfo.setName("Andrei");
        Response updateUserResponse = ApiClient.User.sendPatchUpdateUser(userInfo, token);
        updateUserResponse.then().statusCode(200);
        Assert.assertEquals(updateUserResponse.body().path("success"), true);
        Assert.assertNotNull(updateUserResponse.body().path("user"));
        Assert.assertEquals(updateUserResponse.body().path("user.name"), "Andrei");
    }

    @Test
    @DisplayName("Check successful updating of users email")
    public void checkSuccessfulUpdatingOfEmailWithAuthorization() {

        // Получение данных пользователя
        Response userInfoResponse = ApiClient.User.sendGetUserInfo(token);
        UserModel userInfo = userInfoResponse.as(UserModel.class);

        // Изменение данных пользователя
        userInfo.setEmail("other.rat@ratmail.rat");
        Response updateUserResponse = ApiClient.User.sendPatchUpdateUser(userInfo, token);
        updateUserResponse.then().statusCode(200);
        Assert.assertEquals(updateUserResponse.body().path("success"), true);
        Assert.assertNotNull(updateUserResponse.body().path("user"));
        Assert.assertEquals(updateUserResponse.body().path("user.email"), "other.rat@ratmail.rat");
    }

    @Test
    @DisplayName("Check successful updating of users name")
    public void checkFailureUpdatingOfNameWithoutAuthorization() {

        // Получение данных пользователя
        Response userInfoResponse = ApiClient.User.sendGetUserInfo(token);
        UserModel userInfo = userInfoResponse.as(UserModel.class);

        // Изменение данных пользователя
        userInfo.setName("Andrei");
        Response updateUserResponse = ApiClient.User.sendPatchUpdateUser(userInfo, null);
        updateUserResponse.then().statusCode(401);
        Assert.assertEquals(updateUserResponse.body().path("success"), false);
        Assert.assertEquals(updateUserResponse.body().path("message"), "You should be authorised");
    }

    @Test
    @DisplayName("Check successful updating of users email")
    public void checkFailureUpdatingOfEmailWithoutAuthorization() {

        // Получение данных пользователя
        Response userInfoResponse = ApiClient.User.sendGetUserInfo(token);
        UserModel userInfo = userInfoResponse.as(UserModel.class);

        // Изменение данных пользователя
        userInfo.setEmail("other.rat@ratmail.rat");
        Response updateUserResponse = ApiClient.User.sendPatchUpdateUser(userInfo, null);
        updateUserResponse.then().statusCode(401);
        Assert.assertEquals(updateUserResponse.body().path("success"), false);
        Assert.assertEquals(updateUserResponse.body().path("message"), "You should be authorised");
    }

    @After
    public void clean() {
        // Возвращение тестового окружения к исходному виду
        ApiClient.User.sendDeleteCourier(token);
    }
}
