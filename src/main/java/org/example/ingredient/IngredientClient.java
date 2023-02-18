package org.example.ingredient;

import org.example.client.Client;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.example.ingredient.IngredientEndPoints.GET_INGREDIENTS;


public class IngredientClient extends Client {

    public List<IngredientData> getIngredients() {
        List<IngredientData> ingredientData;
        return given()
                .log().all()
                .spec(getSpec())
                .when()
                .get(GET_INGREDIENTS)
                .then().log().all()
                .extract().body().jsonPath().getList("data", IngredientData.class);
    }
}