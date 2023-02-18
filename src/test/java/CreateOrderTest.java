import io.restassured.response.ValidatableResponse;
import org.example.ingredient.Ingredient;
import org.example.ingredient.IngredientClient;
import org.example.ingredient.IngredientData;
import org.example.order.Order;
import org.example.order.OrderClient;
import org.example.order.OrderGenerator;
import org.example.user.User;
import org.example.user.UserClient;
import org.example.user.UserCredentials;
import org.example.user.UserGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class CreateOrderTest {
    private Order order;
    private OrderClient orderClient;
    private Ingredient ingredient;
    private IngredientClient ingredientClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        ingredientClient = new IngredientClient();
        ingredient = new Ingredient();

        UserClient userClient = new UserClient();
        user = UserGenerator.getUniqueUser();
        userClient.create(user);
        ValidatableResponse loginResponse = userClient.login(UserCredentials.getCredentials(user));
        accessToken = loginResponse.extract().path("accessToken");


    }

    @Test
    public void createNotAuthorizedOrderWithIngredientsShouldSuccessTest() {
        List<IngredientData> data = ingredientClient.getIngredients();
        List<String> ingredientIds = ingredient.getRandomIngredientData(data);

        order = OrderGenerator.getOrderWithIngredients(ingredientIds);
        ValidatableResponse createOrderResponse = orderClient.createNotAuthorized(order);

        int createStatusCode = createOrderResponse.extract().statusCode();

        assertEquals(SC_OK, createStatusCode);
        createOrderResponse.assertThat().body("name", notNullValue());
        createOrderResponse.assertThat().body("order.number", notNullValue());

    }


    @Test
    public void createAuthorizedOrderWithIngredientsShouldSuccessTest() {

        List<IngredientData> data = ingredientClient.getIngredients();
        List<String> ingredientIds = ingredient.getRandomIngredientData(data);

        order = OrderGenerator.getOrderWithIngredients(ingredientIds);

        ValidatableResponse createOrderResponse = orderClient.createAuthorized(order, accessToken);

        int createStatusCode = createOrderResponse.extract().statusCode();

        assertEquals(SC_OK, createStatusCode);

        createOrderResponse.assertThat().body("name", notNullValue());
        createOrderResponse.assertThat().body("order.number", notNullValue());

        String expectedOwnerName = user.getName();
        String actualOwnerName = createOrderResponse.extract().path("order.owner.name");
        assertEquals(expectedOwnerName, actualOwnerName);

        String expectedOwnerEmail = user.getEmail();
        String actualOwnerEmail = createOrderResponse.extract().path("order.owner.email");
        assertEquals(expectedOwnerEmail, actualOwnerEmail);


        List<IngredientData> actualOrderIngredients = createOrderResponse.extract().jsonPath().getList("order.ingredients", IngredientData.class);
        List<String> actualOrderIngredientsIds = actualOrderIngredients.stream().map(IngredientData::get_id).collect(Collectors.toList());

        assertEquals(ingredientIds, actualOrderIngredientsIds);

    }

    @Test
    public void createAuthorizedOrderNoIngredientsShouldFailTest() {

        order = OrderGenerator.getOrderNoIngredients();
        ValidatableResponse createOrderResponse = orderClient.createAuthorized(order, accessToken);

        int createStatusCode = createOrderResponse.extract().statusCode();

        assertEquals(SC_BAD_REQUEST, createStatusCode);
        String expectedMessage = "Ingredient ids must be provided";
        String actualMessage = createOrderResponse.extract().path("message");

        assertEquals(expectedMessage, actualMessage);

    }

    @Test
    public void createAuthorizedOrderInvalidIngredientIdsShouldFailTest() {
        Random random = new Random();
        List<String> ingredientIds = List.of(String.valueOf(random.hashCode()));
        order = OrderGenerator.getOrderWithIngredients(ingredientIds);
        ValidatableResponse createOrderResponse = orderClient.createAuthorized(order, accessToken);

        int createStatusCode = createOrderResponse.extract().statusCode();

        assertEquals(SC_INTERNAL_SERVER_ERROR, createStatusCode);

    }
}
