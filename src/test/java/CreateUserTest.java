import io.restassured.response.ValidatableResponse;
import org.example.user.User;
import org.example.user.UserClient;
import org.example.user.UserGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class CreateUserTest {
    private  User user;
    private  UserClient userClient;

    @Before
    public  void setUp(){
        userClient = new UserClient();
        user = UserGenerator.getUniqueUser();
    }
    @Test
    public void createUniqueUserShouldBeOKTest(){
        user = UserGenerator.getUniqueUser();
        ValidatableResponse response = userClient.create(user);
        int createStatusCode = response.extract().statusCode();

        assertEquals(SC_OK, createStatusCode);
        response.assertThat().body("accessToken", notNullValue());
        response.assertThat().body("refreshToken", notNullValue());

    }

    @Test
    public void createAlreadyRegisteredUserShouldFailTest(){
        user = UserGenerator.getUniqueUser();
        userClient.create(user);
        ValidatableResponse responseNext = userClient.create(user);
        int createStatusCode = responseNext.extract().statusCode();
        String actualMessage = responseNext.extract().path("message");
        String expectedMessage = "User already exists";

        assertEquals(SC_FORBIDDEN, createStatusCode);
        assertEquals(expectedMessage, actualMessage);
    }


}
