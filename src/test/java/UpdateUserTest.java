import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.user.User;
import org.example.user.UserClient;
import org.example.user.UserCredentials;
import org.example.user.UserGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;

public class UpdateUserTest {
    private UserClient userClient;
    private String accessToken;

    @Before
    public  void setUp(){
        userClient = new UserClient();

    }

    @Test
    public void updateAuthorizedUserExistingEmailShouldFailTest(){
        String userEmailInitial =  RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@test.com";
        User userInitial = UserGenerator.getManuallyGeneratedUser(userEmailInitial, "Password", "Name");
        userClient.create(userInitial);

        User userToUpdate = UserGenerator.getUniqueUser();
        userClient.create(userToUpdate);
        ValidatableResponse loginResponse = userClient.login(UserCredentials.getCredentials(userToUpdate));
        accessToken = loginResponse.extract().path("accessToken");

        String updatedUserName = "updatedUserName";
        String updatedUserPassword = "updatedUserPassword";
        ValidatableResponse updateResponse = userClient.updateAuthorizedUserData(User.updateUserData(updatedUserName, userEmailInitial, updatedUserPassword), accessToken);

        int updateStatusCode = updateResponse.extract().statusCode();
        assertEquals(SC_FORBIDDEN, updateStatusCode);

        String expectedMessage = "User with such email already exists";
        String actualMessage = updateResponse.extract().path("message");
        assertEquals(expectedMessage, actualMessage);

    }

    /**
     * В задании сказано -
     * Изменение данных пользователя:
     * с авторизацией,
     * без авторизации,
     * Для обеих ситуаций нужно проверить, что любое поле можно изменить. Для неавторизованного пользователя — ещё и то, что система вернёт ошибку.
     * пароль почему-то в запрсе отсутствует и не меняется. Поэтому тест падает
     */
    @Test
    public void updateAuthorizedUserDataShouldSuccessTest(){

        User user = UserGenerator.getUniqueUser();
        userClient.create(user);
        ValidatableResponse loginResponse = userClient.login(UserCredentials.getCredentials(user));
        accessToken = loginResponse.extract().path("accessToken");

        String updatedUserName = RandomStringUtils.randomAlphabetic(10);
        String updatedUserEmail = RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@test.com";
        String updatedUserPassword = RandomStringUtils.randomNumeric(8);
        ValidatableResponse updateResponse = userClient.updateAuthorizedUserData(User.updateUserData(updatedUserName, updatedUserEmail, updatedUserPassword), accessToken);

        String actualUserEmail = updateResponse.extract().path("user.email");
        String actualUserName = updateResponse.extract().path("user.name");
        String actualUserPassword = updateResponse.extract().path("user.password");
        int updateStatusCode = updateResponse.extract().statusCode();

        assertEquals(SC_OK, updateStatusCode);

        assertEquals(updatedUserName, actualUserName);
        assertEquals(updatedUserEmail, actualUserEmail);
        assertEquals("Password Should be updated", updatedUserPassword, actualUserPassword);

    }

    @Test
    public void updateNotAuthorizedUserDataShouldFailTest(){

        User userNotAuthorized = UserGenerator.getUniqueUser();
        userClient.create(userNotAuthorized);
        String updatedUserName = RandomStringUtils.randomAlphabetic(10);
        String updatedUserEmail = RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@test.com";
        String updatedUserPassword = RandomStringUtils.randomNumeric(8);
        ValidatableResponse updateResponse = userClient.updateNotAuthorizedUserData(User.updateUserData(updatedUserName, updatedUserEmail, updatedUserPassword));

        String expectedMessage = "You should be authorised";
        String actualMessage = updateResponse.extract().path("message");

        int updateStatusCode = updateResponse.extract().statusCode();

        assertEquals(SC_UNAUTHORIZED, updateStatusCode);
        assertEquals(expectedMessage, actualMessage);

    }
}


