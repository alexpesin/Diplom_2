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
import java.util.stream.Collectors;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class GetOrderTest {
    private Order order;
    private OrderClient orderClient;
    private Ingredient ingredient;
    private IngredientClient ingredientClient;
    private String accessToken;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        ingredientClient = new IngredientClient();
        ingredient = new Ingredient();

        UserClient userClient = new UserClient();
        User user = UserGenerator.getUniqueUser();
        userClient.create(user);
        ValidatableResponse loginResponse = userClient.login(UserCredentials.getCredentials(user));
        accessToken = loginResponse.extract().path("accessToken");

    }

    @Test
    public void getOrdersAuthorizedUserShouldSuccessTest(){
        List<IngredientData> ingredientData = ingredientClient.getIngredients();

        List<String> firstOrderIngredientIds = ingredient.getRandomIngredientData(ingredientData);
        List<String> secondIngredientIds = ingredient.getRandomIngredientData(ingredientData);

        Order firstUserOrder = OrderGenerator.getOrderWithIngredients(firstOrderIngredientIds);
        Order secondUserOrder = OrderGenerator.getOrderWithIngredients(secondIngredientIds);

        orderClient.createAuthorized(firstUserOrder, accessToken);
        orderClient.createAuthorized(secondUserOrder, accessToken);
        ValidatableResponse getResponse = orderClient.getAuthorized(accessToken);

        int getStatusCode = getResponse.extract().statusCode();

        assertEquals(SC_OK, getStatusCode);
        getResponse.assertThat().body("total", notNullValue());
        getResponse.assertThat().body("totalToday", notNullValue());

        List<Order> actualUserOrders = getResponse.extract().jsonPath().getList("orders", Order.class);
        List<String> actualOrderIds = actualUserOrders.stream().map(Order::get_id).collect(Collectors.toList());

        int expectedOrderListSize = 2;
        int actualOrderListSize = actualOrderIds.size();
        assertEquals(expectedOrderListSize, actualOrderListSize);

    }

    @Test
    public void getOrdersNotAuthorizedUserShouldFailTest(){
        List<IngredientData> ingredientData = ingredientClient.getIngredients();

        List<String> firstOrderIngredientIds = ingredient.getRandomIngredientData(ingredientData);
        List<String> secondIngredientIds = ingredient.getRandomIngredientData(ingredientData);

        Order firstUserOrder = OrderGenerator.getOrderWithIngredients(firstOrderIngredientIds);
        Order secondUserOrder = OrderGenerator.getOrderWithIngredients(secondIngredientIds);

        orderClient.createAuthorized(firstUserOrder, accessToken);
        orderClient.createAuthorized(secondUserOrder, accessToken);

        ValidatableResponse getResponse = orderClient.getNotAuthorized();

        int getStatusCode = getResponse.extract().statusCode();

        assertEquals(SC_UNAUTHORIZED, getStatusCode);
        String expectedMessage = "You should be authorised";
        String actualMessage = getResponse.extract().path("message");

        assertEquals(expectedMessage, actualMessage);

    }
}

