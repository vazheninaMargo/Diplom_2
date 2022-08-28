package orders;

import common.TestsHelper;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import praktikum.IngredientsModel;
import praktikum.LoginUserResponseModel;
import praktikum.UserCreateModel;
import praktikum.UserLoginModel;
import user.UserTestsHelper;

public class CreateOrderTests {

    private final static String email = "rat@ratmail.rat";
    private final static String password = "1234";
    private static String token;

    private static IngredientsModel emptyIngredientsModel;
    private static IngredientsModel correctIngredientsModel;
    private static IngredientsModel incorrectIngredientsModel;

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
        Response loginResponse = UserTestsHelper.sendPostLoginCourier(userLoginModel);
        token = loginResponse.body().as(LoginUserResponseModel.class).getAccessToken();
        String[] emptyIngredients = {};
        String[] correctIngredients = {
                "61c0c5a71d1f82001bdaaa6d",
                "61c0c5a71d1f82001bdaaa6f",
                "61c0c5a71d1f82001bdaaa72"
        };
        String[] incorrectIngredients = {
                "$%_61c0c5a71d1f82001bdaaa6d",
                "$%_61c0c5a71d1f82001bdaaa6f",
                "$%_61c0c5a71d1f82001bdaaa72"
        };
        emptyIngredientsModel = new IngredientsModel(emptyIngredients);
        correctIngredientsModel = new IngredientsModel(correctIngredients);
        incorrectIngredientsModel = new IngredientsModel(incorrectIngredients);
    }

    @Test
    @DisplayName("Check creating order with auth")
    public void checkCreatingOrderWithAuth() {

        // Получение заказов пользователя
        Response ingredientsListResponse = OrdersTestsHelper.sendPostCreateOrder(correctIngredientsModel, token);
        TestsHelper.compareResponseCode(ingredientsListResponse, 200);
    }

    @Test
    @DisplayName("Check failure creating order without auth")
    public void checkCreatingOrderWithoutAuth() {

        // Получение заказов пользователя
        Response ingredientsListResponse = OrdersTestsHelper.sendPostCreateOrder(correctIngredientsModel, null);
        TestsHelper.compareResponseCode(ingredientsListResponse, 401);
    }

    @Test
    @DisplayName("Check failure creating order with auth. Without ingredients")
    public void checkCreatingOrderWithAuthWithoutIngredients() {

        // Получение заказов пользователя
        Response ingredientsListResponse = OrdersTestsHelper.sendPostCreateOrder(emptyIngredientsModel, token);
        TestsHelper.compareResponseCode(ingredientsListResponse, 400);
    }

    @Test
    @DisplayName("Check failure creating order without auth. Without ingredients")
    public void checkCreatingOrderWithoutAuthWithoutIngredients() {

        // Получение заказов пользователя
        Response ingredientsListResponse = OrdersTestsHelper.sendPostCreateOrder(emptyIngredientsModel, null);
        TestsHelper.compareResponseCode(ingredientsListResponse, 401);
    }

    @Test
    @DisplayName("Check failure creating order with auth. With incorrect ingredients")
    public void checkCreatingOrderWithAuthWithIncorrectIngredients() {

        // Получение заказов пользователя
        Response ingredientsListResponse = OrdersTestsHelper.sendPostCreateOrder(incorrectIngredientsModel, token);
        TestsHelper.compareResponseCode(ingredientsListResponse, 500);
    }

    @Test
    @DisplayName("Check failure creating order without auth. With incorrect ingredients")
    public void checkCreatingOrderWithoutAuthWithoutWithIncorrectIngredients() {

        // Получение заказов пользователя
        Response ingredientsListResponse = OrdersTestsHelper.sendPostCreateOrder(incorrectIngredientsModel, null);
        TestsHelper.compareResponseCode(ingredientsListResponse, 401);
    }

    @After
    public void clean() {
        // Возвращение тестового окружения к исходному виду
        UserTestsHelper.sendDeleteCourier(token);
    }
}
