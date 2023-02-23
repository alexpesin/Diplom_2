import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.user.User;
import org.example.user.UserClient;
import org.example.user.UserGenerator;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;

public class CreateUserTest {
    private User user;
    private UserClient userClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка, что уникальный пользователь успешно создался")
    public void createUniqueUserShouldBeOKTest() throws IOException {
        user = UserGenerator.getUniqueUser();
        ValidatableResponse response = userClient.create(user);
        int createStatusCode = response.extract().statusCode();

        user.checkStatusCodeEqualsStep(SC_OK, createStatusCode);

        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        user.checkResponseBodyContainsParameter(response, accessToken);
        user.checkResponseBodyContainsParameter(response, refreshToken);

    }


    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован вызывает ошибку")
    @Description("Проверка, что нельзя создать пользователя, который уже зарегистрирован")
    public void createAlreadyRegisteredUserShouldFailTest() throws IOException {
        user = UserGenerator.getUniqueUser();
        userClient.create(user);
        ValidatableResponse responseNext = userClient.create(user);
        int createStatusCode = responseNext.extract().statusCode();
        String actualMessage = responseNext.extract().path("message");
        String expectedMessage = "User already exists";

        user.checkStatusCodeEqualsStep(SC_FORBIDDEN, createStatusCode);
        user.checkStringEqualsStep(expectedMessage, actualMessage);

    }


}
