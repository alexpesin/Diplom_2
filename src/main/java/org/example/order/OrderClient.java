package org.example.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.client.Client;

import static io.restassured.RestAssured.given;
import static org.example.order.OrderEndPoints.CREATE_ORDER;
import static org.example.order.OrderEndPoints.GET_ORDER;


public class OrderClient extends Client {
    @Step("Создание заказа неавторизованным пользователем")
    public ValidatableResponse createNotAuthorized(Order order) {
        return given()
                .log().all()
                .spec(getSpec())
                .body(order)
                .when()
                .post(CREATE_ORDER)
                .then()
                .log().all();
    }

    @Step("Создание заказа авторизованным пользователем")
    public ValidatableResponse createAuthorized(Order order, String accessToken) {
        return given()
                .log().all()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(CREATE_ORDER)
                .then()
                .log().all();
    }

    public ValidatableResponse getAuthorized(String accessToken) {
        return given()
                .log().all()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .when()
                .get(GET_ORDER)
                .then()
                .log().all();
    }

    public ValidatableResponse getNotAuthorized() {
        return given()
                .log().all()
                .spec(getSpec())
                .when()
                .get(GET_ORDER)
                .then()
                .log().all();
    }


}
