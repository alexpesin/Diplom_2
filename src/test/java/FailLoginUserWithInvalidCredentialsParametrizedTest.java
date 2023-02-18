import io.restassured.response.ValidatableResponse;
import org.example.user.User;
import org.example.user.UserClient;
import org.example.user.UserCredentials;
import org.example.user.UserGenerator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

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
    public static void setUp(){
        userClient = new UserClient();

    }

    @Parameterized.Parameters
    public static Object[][] getUserData(){
        return new Object[][]{
                {"", "", ""},
                {"wrongEmail@noemail.com", "", ""},
                {"", "WrongP@ssword!", ""},
                {"wrongEmail@noemail.com", "WrongP@ssword!", ""},

        };
    }
    @Test
    public void loginAsInvalidCredentialsUserShouldFailTest(){
        User user = UserGenerator.getManuallyGeneratedUser(email, password, name);
        ValidatableResponse loginResponse = userClient.login(UserCredentials.getCredentials(user));

        int statusCode = loginResponse.extract().statusCode();
        String actualMessage = loginResponse.extract().path("message");
        String expectedMessage = "email or password are incorrect";

        assertEquals(SC_UNAUTHORIZED, statusCode);
        assertEquals(expectedMessage, actualMessage);

    }
}
