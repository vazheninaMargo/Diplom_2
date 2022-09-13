package orders;

import api.client.login.LoginResponseModel;
import api.client.registration.RegistrationModel;
import api.client.login.LoginModel;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.ApiClient;

public class GetOrdersTests {

    private final static String email = "rat8@ratmail.rat";
    private final static String password = "1234";
    private final static String name = "Oleg";
    private static String token;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

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
    @DisplayName("Check successful receiving of users orders")
    public void checkReceivingOrdersWithAuth() {

        // Получение заказов пользователя
        Response ordersResponse = ApiClient.Order.sendGetOrdersList(token);
        ordersResponse.then().statusCode(200);
        Assert.assertEquals(ordersResponse.body().path("success"), true);
        Assert.assertNotNull(ordersResponse.body().path("orders"));
        Assert.assertNotNull(ordersResponse.body().path("total"));
        Assert.assertNotNull(ordersResponse.body().path("totalToday"));
    }

    @Test
    @DisplayName("Check successful receiving of users orders")
    public void checkReceivingOrdersWithoutAuth() {

        // Получение заказов пользователя
        Response ordersResponse = ApiClient.Order.sendGetOrdersList(null);
        ordersResponse.then().statusCode(401);
        Assert.assertEquals(ordersResponse.body().path("success"), false);
        Assert.assertEquals(ordersResponse.body().path("message"), "You should be authorised");
    }

    @After
    public void clean() {
        // Возвращение тестового окружения к исходному виду
        ApiClient.User.sendDeleteCourier(token);
    }
}
