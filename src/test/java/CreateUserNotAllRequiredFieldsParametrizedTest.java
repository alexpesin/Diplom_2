import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.user.User;
import org.example.user.UserClient;
import org.example.user.UserGenerator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;

@RunWith(Parameterized.class)
public class CreateUserNotAllRequiredFieldsParametrizedTest {

    private static UserClient userClient;

    private final String email;
    private final String password;
    private final String name;

    public CreateUserNotAllRequiredFieldsParametrizedTest(String email, String password, String name) {
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
                {"", "", "Name"},
                {"", "password", "Name"},
                {"test@test.com", "", "Name"},
                {"test@test.com", "password", ""},

        };
    }

    @Test
    @DisplayName("Создание пользователя c незаполненными обязательными полями вызывает ошибку")
    @Description("Проверка, что нельзя создать пользователя если не заполнить одно из обязательных полей")
    public void createUserNotAllRequiredFieldsShouldFailTest() throws IOException {
        User user = UserGenerator.getManuallyGeneratedUser(email, password, name);
        ValidatableResponse createResponse = userClient.create(user);
        int statusCode = createResponse.extract().statusCode();
        String actualMessage = createResponse.extract().path("message");
        String expectedMessage = "Email, password and name are required fields";

        user.checkStatusCodeEqualsStep(SC_FORBIDDEN, statusCode);

        user.checkStringEqualsStep(expectedMessage, actualMessage);


    }
}
