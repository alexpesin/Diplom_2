package org.example.user;

import io.restassured.response.ValidatableResponse;
import org.example.client.Client;

import static io.restassured.RestAssured.given;
import static org.example.user.UserEndPoints.*;

public class UserClient extends Client {

    public ValidatableResponse create(User user){
        return given()
                .log().all()
                .spec(getSpec())
                .body(user)
                .when()
                .post(CREATE_USER)
                .then()
                .log().all();
    }

    public ValidatableResponse login(UserCredentials credentials){
        return given()
                .log().all()
                .spec(getSpec())
                .body(credentials)
                .when()
                .post(LOGIN_USER)
                .then()
                .log().all();
    }

    /*public ValidatableResponse getUserData(String accessToken){
        return given()
                .log().all()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .when()
                .get(GET_USER_DATA)
                .then()
                .log().all();
    }*/

    public ValidatableResponse updateAuthorizedUserData(User user, String accessToken){
        return given()
                .log().all()
                .spec(getSpec())
                .body(user)
                .header("Authorization", accessToken)
                .when()
                .patch(UPDATE_USER)
                .then()
                .log().all();
    }

    public ValidatableResponse updateNotAuthorizedUserData(User user){
        return given()
                .log().all()
                .spec(getSpec())
                .body(user)
                .when()
                .patch(UPDATE_USER)
                .then()
                .log().all();
    }

    public ValidatableResponse delete(){
        return given()
                .spec(getSpec())
                .log().all()
                .when()
                .delete(DELETE_USER)
                .then()
                .log().all();
    }
}
