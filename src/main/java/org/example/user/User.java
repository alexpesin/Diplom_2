package org.example.user;

/*
 Recording
  DTO
  Clients
  Generations
  Clean up
*/

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class User {
    private String email;
    private String password;
    private String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User() {
    }

    @Step("Редактирование данных пользователя")
    public static User updateUserData(String name, String email, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Step("Проверка что ожидаемый код ошибки {expectedCode} эквивалентен актуальному {actualCode}")
    public void checkStatusCodeEqualsStep(int expectedCode, int actualCode) {
        assertEquals("Коды не эквивалентны", expectedCode, actualCode);

    }

    @Step("Проверка что ожидаемое сообщение: '{str1}' эквивалентно  актуальному: '{str2}'")
    public void checkStringEqualsStep(String str1, String str2) {
        assertTrue("Строки не эквивалентны", str1.equals(str2));

    }

    @Step("Проверка что в ответе есть непустой параметр {param}")
    public void checkResponseBodyContainsParameter(ValidatableResponse response, String param) {
        response.assertThat().body(param, notNullValue());
    }
}
