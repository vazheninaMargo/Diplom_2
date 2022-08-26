package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import praktikum.LoginUserResponseModel;
import praktikum.UserCreateModel;
import praktikum.UserInfoModel;
import praktikum.UserLoginModel;

public class UserChangeTests {

    private final String email = "rat@ratmail.rat";
    private final String password = "1234";
    private final String name = "Oleg";
    private String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

        UserCreateModel userCreateModel = new UserCreateModel(
                email,
                password,
                "Oleg"
        );
        UserLoginModel userLoginModel = new UserLoginModel(
                email,
                password
        );
        UserTestsHelper.sendPostCreateUser(userCreateModel);
        Response loginResponse = UserTestsHelper.sendPostLoginCourier(userLoginModel);
        token = loginResponse.body().as(LoginUserResponseModel.class).getAccessToken();
    }

    @Test
    @DisplayName("Check successful updating of users name")
    public void checkSuccessfulUpdatingOfNameWithAuthorization() {

        // Получение данных пользователя
        Response userInfoResponse = UserTestsHelper.sendGetUserInfo(token);
        UserInfoModel userInfo = userInfoResponse.as(UserInfoModel.class);

        // Изменение данных пользователя
        userInfo.setName("Andrei");
        Response updateUserResponse = UserTestsHelper.sendPatchUpdateUser(userInfo, token);
        UserTestsHelper.compareResponseCode(updateUserResponse, 200);
    }

    @Test
    @DisplayName("Check successful updating of users email")
    public void checkSuccessfulUpdatingOfEmailWithAuthorization() {

        // Получение данных пользователя
        Response userInfoResponse = UserTestsHelper.sendGetUserInfo(token);
        UserInfoModel userInfo = userInfoResponse.as(UserInfoModel.class);

        // Изменение данных пользователя
        userInfo.setEmail("other.rat@ratmail.rat");
        Response updateUserResponse = UserTestsHelper.sendPatchUpdateUser(userInfo, token);
        UserTestsHelper.compareResponseCode(updateUserResponse, 200);
    }

    @Test
    @DisplayName("Check successful updating of users name")
    public void checkFailureUpdatingOfNameWithoutAuthorization() {

        // Получение данных пользователя
        Response userInfoResponse = UserTestsHelper.sendGetUserInfo(token);
        UserInfoModel userInfo = userInfoResponse.as(UserInfoModel.class);

        // Изменение данных пользователя
        userInfo.setName("Andrei");
        Response updateUserResponse = UserTestsHelper.sendPatchUpdateUser(userInfo, null);
        UserTestsHelper.compareResponseCode(updateUserResponse, 401);
    }

    @Test
    @DisplayName("Check successful updating of users email")
    public void checkFailureUpdatingOfEmailWithoutAuthorization() {

        // Получение данных пользователя
        Response userInfoResponse = UserTestsHelper.sendGetUserInfo(token);
        UserInfoModel userInfo = userInfoResponse.as(UserInfoModel.class);

        // Изменение данных пользователя
        userInfo.setEmail("other.rat@ratmail.rat");
        Response updateUserResponse = UserTestsHelper.sendPatchUpdateUser(userInfo, null);
        UserTestsHelper.compareResponseCode(updateUserResponse, 401);
    }

    @After
    public void clean() {
        // Возвращение тестового окружения к исходному виду
            UserTestsHelper.sendDeleteCourier(token);
    }
}
