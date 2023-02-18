import io.restassured.response.ValidatableResponse;
import org.example.user.User;
import org.example.user.UserClient;
import org.example.user.UserCredentials;
import org.example.user.UserGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class SuccessLoginUserTest {

    private User user;
    private UserClient userClient;


    @Before
    public  void setUp(){
        userClient = new UserClient();
        user = UserGenerator.getUniqueUser();
        userClient.create(user);

    }

    @Test
    public void loginAsExistingUserShouldBeOKTest(){

        ValidatableResponse loginResponse = userClient.login(UserCredentials.getCredentials(user));
        int loginStatusCode = loginResponse.extract().statusCode();
        String expectedUserEmail = user.getEmail();
        String actualUserEmail = loginResponse.extract().path("user.email");

        String expectedUserName = user.getName();
        String actualUserName = loginResponse.extract().path("user.name");

        assertEquals(SC_OK, loginStatusCode);

        loginResponse.assertThat().body("accessToken", notNullValue());
        loginResponse.assertThat().body("refreshToken", notNullValue());

        assertEquals(expectedUserEmail, actualUserEmail);
        assertEquals(expectedUserName, actualUserName);

    }
}
