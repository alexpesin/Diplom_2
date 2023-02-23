package org.example.order;


import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.ingredient.IngredientData;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Order {
    private String name;
    private String _id;
    private List<OrderOwnerData> data;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String number;
    private String price;
    private List<String> ingredients;
    private List<IngredientData> ingredientData;


    public Order(String name, String _id, List<OrderOwnerData> data, String status, String createdAt, String updatedAt, String number, String price, List<String> ingredients) {
        this.name = name;
        this._id = _id;
        this.data = data;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.number = number;
        this.price = price;
        this.ingredients = ingredients;
    }

    public Order() {
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<IngredientData> getIngredientData() {
        return ingredientData;
    }

    public String get_id() {
        return _id;
    }

    @Step("Проверка что ожидаемый код ошибки {expectedCode} эквивалентен актуальному {actualCode}")
    public void checkStatusCodeEqualsStep(int expectedCode, int actualCode) {
        assertEquals("Коды не эквивалентны", expectedCode, actualCode);
    }

    @Step("Проверка что в ответе есть непустой параметр {param}")
    public void checkResponseBodyContainsParameter(ValidatableResponse response, String param) {
        response.assertThat().body(param, notNullValue());
    }

    @Step("Проверка что ожидаемое сообщение: '{str1}' эквивалентно  актуальному: '{str2}'")
    public void checkStringEqualsStep(String str1, String str2) {
        assertTrue("Строки не эквивалентны", str1.equals(str2));

    }
}
