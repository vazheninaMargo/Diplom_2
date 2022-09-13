package orders;

import api.client.order.IngredientsModel;
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

public class CreateOrderTests {

    private final static String email = "rat8@ratmail.rat";
    private final static String password = "123456";
    private static String token;

    private static IngredientsModel emptyIngredientsModel;
    private static IngredientsModel correctIngredientsModel;
    private static IngredientsModel incorrectIngredientsModel;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

        RegistrationModel userCreateModel = new RegistrationModel(
                email,
                password,
                "Oleg"
        );
        LoginModel userLoginModel = new LoginModel(
                email,
                password
        );
        ApiClient.User.sendPostCreateUser(userCreateModel);
        Response loginResponse = ApiClient.User.sendPostLoginUser(userLoginModel);
        token = loginResponse.body().as(LoginResponseModel.class).getAccessToken();
        String[] emptyIngredients = {};
        String[] correctIngredients = {
                "61c0c5a71d1f82001bdaaa6d",
                "61c0c5a71d1f82001bdaaa6f",
                "61c0c5a71d1f82001bdaaa72"
        };
        String[] incorrectIngredients = {
                "$%_1",
                "$%_2",
                "$%_3"
        };
        emptyIngredientsModel = new IngredientsModel(emptyIngredients);
        correctIngredientsModel = new IngredientsModel(correctIngredients);
        incorrectIngredientsModel = new IngredientsModel(incorrectIngredients);
    }

    @Test
    @DisplayName("Check creating order with auth")
    public void checkCreatingOrderWithAuth() {

        // Получение заказов пользователя
        Response ingredientsListResponse = ApiClient.Order.sendPostCreateOrder(correctIngredientsModel, token);
        ingredientsListResponse.then().statusCode(200);
        Assert.assertEquals(ingredientsListResponse.body().path("success"), true);
        Assert.assertNotNull(ingredientsListResponse.body().path("name"));
        Assert.assertNotNull(ingredientsListResponse.body().path("order"));
    }

    @Test
    @DisplayName("Check failure creating order without auth")
    public void checkCreatingOrderWithoutAuth() {

        // Получение заказов пользователя
        Response ingredientsListResponse = ApiClient.Order.sendPostCreateOrder(correctIngredientsModel, null);
        ingredientsListResponse.then().statusCode(400);
        Assert.assertEquals(ingredientsListResponse.body().path("success"), false);
        Assert.assertEquals(ingredientsListResponse.body().path("message"), "Ingredient ids must be provided");
    }

    @Test
    @DisplayName("Check failure creating order with auth. Without ingredients")
    public void checkCreatingOrderWithAuthWithoutIngredients() {

        // Получение заказов пользователя
        Response ingredientsListResponse = ApiClient.Order.sendPostCreateOrder(emptyIngredientsModel, token);
        ingredientsListResponse.then().statusCode(400);
        Assert.assertEquals(ingredientsListResponse.body().path("success"), false);
        Assert.assertEquals(ingredientsListResponse.body().path("message"), "Ingredient ids must be provided");
    }

    @Test
    @DisplayName("Check failure creating order without auth. Without ingredients")
    public void checkCreatingOrderWithoutAuthWithoutIngredients() {

        // Получение заказов пользователя
        Response ingredientsListResponse = ApiClient.Order.sendPostCreateOrder(emptyIngredientsModel, null);
        ingredientsListResponse.then().statusCode(400);
        Assert.assertEquals(ingredientsListResponse.body().path("success"), false);
        Assert.assertEquals(ingredientsListResponse.body().path("message"), "Ingredient ids must be provided");
    }

    @Test
    @DisplayName("Check failure creating order with auth. With incorrect ingredients")
    public void checkCreatingOrderWithAuthWithIncorrectIngredients() {

        // Получение заказов пользователя
        Response ingredientsListResponse = ApiClient.Order.sendPostCreateOrder(incorrectIngredientsModel, token);
        ingredientsListResponse.then().statusCode(500);
    }

    @Test
    @DisplayName("Check failure creating order without auth. With incorrect ingredients")
    public void checkCreatingOrderWithoutAuthWithoutWithIncorrectIngredients() {

        // Получение заказов пользователя
        Response ingredientsListResponse = ApiClient.Order.sendPostCreateOrder(incorrectIngredientsModel, null);
        ingredientsListResponse.then().statusCode(400);
        Assert.assertEquals(ingredientsListResponse.body().path("success"), false);
        Assert.assertEquals(ingredientsListResponse.body().path("message"), "Ingredient ids must be provided");
    }

    @AfterClass
    public static void clean() {
        // Возвращение тестового окружения к исходному виду
        ApiClient.User.sendDeleteCourier(token);
    }
}
