package org.example.order;

import java.util.List;

public class OrderGenerator {


    public static Order getOrderWithIngredients(List<String> ingredientIds){
        Order order = new Order();
        order.setIngredients(ingredientIds);
        return order;
    }

    public static Order getOrderNoIngredients(){
        return new Order();
    }

}
