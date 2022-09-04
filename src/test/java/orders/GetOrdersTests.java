package orders;

import common.TestsHelper;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import praktikum.LoginUserResponseModel;
import praktikum.UserCreateModel;
import praktikum.UserLoginModel;
import user.UserTestsHelper;

public class GetOrdersTests {

    private final static String email = "rat8@ratmail.rat";
    private final static String password = "1234";
    private final static String name = "Oleg";
    private static String token;

    @BeforeClass
    public static void setUp() {
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
        Response loginResponse = UserTestsHelper.sendPostLoginUser(userLoginModel);
        token = loginResponse.body().as(LoginUserResponseModel.class).getAccessToken();
    }

    @Test
    @DisplayName("Check successful receiving of users orders")
    public void checkReceivingOrdersWithAuth() {

        // Получение заказов пользователя
        Response ordersResponse = OrdersTestsHelper.sendGetOrdersList(token);
        TestsHelper.compareResponseCode(ordersResponse, 200);
    }

    @Test
    @DisplayName("Check successful receiving of users orders")
    public void checkReceivingOrdersWithoutAuth() {

        // Получение заказов пользователя
        Response ordersResponse = OrdersTestsHelper.sendGetOrdersList(null);
        TestsHelper.compareResponseCode(ordersResponse, 401);
    }

    @After
    public void clean() {
        // Возвращение тестового окружения к исходному виду
        UserTestsHelper.sendDeleteCourier(token);
    }
}
