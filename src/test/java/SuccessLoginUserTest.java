import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.user.User;
import org.example.user.UserClient;
import org.example.user.UserCredentials;
import org.example.user.UserGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;

public class SuccessLoginUserTest {

    private User user;
    private UserClient userClient;


    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getUniqueUser();
        userClient.create(user);

    }

    @Test
    @DisplayName("логин под существующим пользователем с валидными данными")
    @Description("Проверка, что пользователь с валидным логином и паролем успешно логинится")
    public void loginAsExistingUserShouldBeOKTest() {

        ValidatableResponse loginResponse = userClient.login(UserCredentials.getCredentials(user));
        int loginStatusCode = loginResponse.extract().statusCode();
        String expectedUserEmail = user.getEmail();
        String actualUserEmail = loginResponse.extract().path("user.email");

        String expectedUserName = user.getName();
        String actualUserName = loginResponse.extract().path("user.name");
        user.checkStatusCodeEqualsStep(SC_OK, loginStatusCode);
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        user.checkResponseBodyContainsParameter(loginResponse, accessToken);
        user.checkResponseBodyContainsParameter(loginResponse, refreshToken);

        user.checkStringEqualsStep(expectedUserEmail, actualUserEmail);
        user.checkStringEqualsStep(expectedUserName, actualUserName);

    }
}
