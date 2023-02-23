package org.example.order;

import io.qameta.allure.Step;

import java.util.List;

public class OrderGenerator {

    @Step("Генерация заказа с ингредиентами")
    public static Order getOrderWithIngredients(List<String> ingredientIds) {
        Order order = new Order();
        order.setIngredients(ingredientIds);
        return order;
    }

    @Step("Генерация заказа без ингредиентов")
    public static Order getOrderNoIngredients() {
        return new Order();
    }

}
