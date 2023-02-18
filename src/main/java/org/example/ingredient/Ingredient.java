package org.example.ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Ingredient {
    private List<IngredientData> ingredientData;

    public Ingredient(List<IngredientData> ingredientData) {
        this.ingredientData = ingredientData;
    }

    public Ingredient() {
    }

    public List<IngredientData> getIngredientData() {
        return ingredientData;
    }

    public void setIngredientData(List<IngredientData> ingredientData) {
        this.ingredientData = ingredientData;
    }

    public List<String> getRandomIngredientData(List<IngredientData>data){

        List<String> ids = data.stream().map(IngredientData::get_id).collect(Collectors.toList());
        List<String> randomIngredients = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int v = (int) (Math.random() * (ids.size() - 1));
            randomIngredients.add(ids.get(v));

        }
        return randomIngredients;
    }
}
