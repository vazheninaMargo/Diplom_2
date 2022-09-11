package utils;

import api.client.order.IngredientsModel;
import api.client.registration.RegistrationModel;
import api.models.UserModel;
import api.client.login.LoginModel;
import io.qameta.allure.Step;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiClient {
    public static class Order {
        @Step("Send GET request to /api/orders")
        static public Response sendGetOrdersList(String token) {
            Map<String, Object> headers = new HashMap<>();
            if (token != null) {
                headers.put("Authorization", token);
            }

            return given()
                    .headers(headers)
                    .filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
                    .when()
                    .get("/api/orders");
        }

        @Step("Send POST request to /api/orders")
        static public Response sendPostCreateOrder(IngredientsModel ingredients, String token) {
            Map<String, Object> headers = new HashMap<>();
            if (token != null) {
                headers.put("Authorization", token);
                headers.put("Content-type", "application/json");
            }

            return given()
                    .headers(headers)
                    .filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
                    .body(ingredients)
                    .when()
                    .post("/api/orders");
        }
    }

    public static class User {
        @Step("Send POST request to /api/auth/register")
        static public Response sendPostCreateUser(RegistrationModel model) {
            return given()
                    .filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
                    .body(model)
                    .when()
                    .header("Content-type", "application/json")
                    .post("/api/auth/register");
        }

        @Step("Send POST request to /api/auth/login")
        static public Response sendPostLoginUser(LoginModel model) {
            return given()
                    .filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
                    .body(model)
                    .when()
                    .header("Content-type", "application/json")
                    .post("/api/auth/login");
        }

        @Step("Send PATCH request to /api/auth/user")
        static public Response sendPatchUpdateUser(UserModel model, String token) {
            Map<String, Object> headers = new HashMap<>();
            headers.put("Authorization", token);
            headers.put("Content-type", "application/json");

            return given()
                    .headers(headers)
                    .filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
                    .body(model)
                    .when()
                    .patch("/api/auth/user");
        }

        @Step("Send GET request to /api/auth/user")
        static public Response sendGetUserInfo(String token) {
            Map<String, Object> headers = new HashMap<>();
            if (token != null) {
                headers.put("Authorization", token);
            }

            return given()
                    .headers(headers)
                    .filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
                    .when()
                    .get("api/auth/user");
        }

        @Step("Send DELETE request to /api/auth/user/:id")
        static public Response sendDeleteCourier(String token) {
            return given()
                    .header("Authorization", token)
                    .filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
                    .when()
                    .delete("/api/auth/user");
        }
    }
}