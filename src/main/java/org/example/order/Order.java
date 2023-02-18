package org.example.order;


import org.example.ingredient.IngredientData;

import java.util.List;

public class Order {
    private String name;
    private String _id;
    private List<OrderOwnerData>data;
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
}
