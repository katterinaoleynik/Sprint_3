package api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import qa.scooter_praktikum_services_tests.CreateOrderTest;

import static io.restassured.RestAssured.given;

public class OrderService {

    public static Response create(CreateOrderTest createOrder) {
        return given()
                .contentType(ContentType.JSON)
                .body(createOrder)
                .post("https://qa-scooter.praktikum-services.ru/api/v1/orders");
    }

    public static Response getList() {
        return given()
                .contentType(ContentType.JSON)
                .get("https://qa-scooter.praktikum-services.ru/api/v1/orders");
    }
}