package common;

import io.qameta.allure.Step;
import io.restassured.response.Response;

public class TestsHelper {
    @Step("Compare response status code")
    static public void compareResponseCode(Response response, int expectedCode) {
        response.then().statusCode(expectedCode);
    }
}
