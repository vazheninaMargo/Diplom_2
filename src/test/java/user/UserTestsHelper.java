package user;

import io.qameta.allure.Step;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import praktikum.UserCreateModel;
import praktikum.UserLoginModel;

import static io.restassured.RestAssured.given;

public class UserTestsHelper {
    @Step("Send POST request to /api/auth/register")
    static public Response sendPostCreateUser(UserCreateModel model) {
        return given()
                .filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
                .body(model)
                .when()
                .header("Content-type", "application/json")
                .post("/api/auth/register");
    }

    @Step("Send POST request to /api/auth/login")
    static public Response sendPostLoginCourier(UserLoginModel model) {
        return given()
                .filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
                .body(model)
                .when()
                .header("Content-type", "application/json")
                .post("/api/auth/login");
    }

    @Step("Send DELETE request to /api/auth/user/:id")
    static public Response sendDeleteCourier(String token) {
        return given()
                .header("Authorization", token)
                .filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
                .when()
                .delete("/api/auth/user");
    }

    @Step("Compare response status code")
    static public void compareResponseCode(Response response, int expectedCode) {
        response.then().statusCode(expectedCode);
    }
}