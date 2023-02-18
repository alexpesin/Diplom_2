import io.restassured.response.ValidatableResponse;
import org.example.user.User;
import org.example.user.UserClient;
import org.example.user.UserGenerator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.junit.Assert.assertEquals;

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
    public static void setUp(){
        userClient = new UserClient();

    }
    @Parameterized.Parameters
    public static Object[][] getUserData(){
        return new Object[][]{

                {"", "", ""},
                {"", "", "Name"},
                {"", "password", "Name"},
                {"test@test.com", "", "Name"},
                {"test@test.com", "password", ""},

        };
    }

    @Test
    public void createUserNotAllRequiredFieldsShouldFailTest(){
        User user = UserGenerator.getManuallyGeneratedUser(email, password, name);
        ValidatableResponse createResponse = userClient.create(user);
        int statusCode = createResponse.extract().statusCode();
        String actualMessage = createResponse.extract().path("message");
        String expectedMessage = "Email, password and name are required fields";

        assertEquals(SC_FORBIDDEN, statusCode);
        assertEquals(expectedMessage, actualMessage);
    }
}
