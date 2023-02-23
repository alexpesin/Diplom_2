import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.user.User;
import org.example.user.UserClient;
import org.example.user.UserCredentials;
import org.example.user.UserGenerator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

@RunWith(Parameterized.class)
public class FailLoginUserWithInvalidCredentialsParametrizedTest {

    private static UserClient userClient;
    private final String email;
    private final String password;
    private final String name;

    public FailLoginUserWithInvalidCredentialsParametrizedTest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @BeforeClass
    public static void setUp() {
        userClient = new UserClient();

    }

    @Parameterized.Parameters(name = "Тестовые данные: {0} {1} {2}")
    public static Object[][] getUserData() {
        return new Object[][]{
                {"", "", ""},
                {"wrongEmail@noemail.com", "", ""},
                {"", "WrongP@ssword!", ""},
                {"wrongEmail@noemail.com", "WrongP@ssword!", ""},

        };
    }

    @Test
    @DisplayName("Логин с неверными данными вызывает ошибку")
    @Description("Проверка, что нельзя залогинится пользователем с неверным логином и/или паролем")
    public void loginAsInvalidCredentialsUserShouldFailTest() throws IOException {
        User user = UserGenerator.getManuallyGeneratedUser(email, password, name);
        ValidatableResponse loginResponse = userClient.login(UserCredentials.getCredentials(user));

        int statusCode = loginResponse.extract().statusCode();
        String actualMessage = loginResponse.extract().path("message");
        String expectedMessage = "email or password are incorrect";

        user.checkStatusCodeEqualsStep(SC_UNAUTHORIZED, statusCode);
        user.checkStringEqualsStep(expectedMessage, actualMessage);


    }
}
