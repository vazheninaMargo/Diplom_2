package orders;

import io.qameta.allure.Step;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrdersTestsHelper {

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
}
