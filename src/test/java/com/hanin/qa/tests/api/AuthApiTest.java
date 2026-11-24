package com.hanin.qa.tests.api;

import com.hanin.qa.api.NotesApiClient;
import com.hanin.qa.utils.TestData;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Notes App API")
@Feature("User Authentication")
public class AuthApiTest {

    private final NotesApiClient api = new NotesApiClient();

    @Test
    @Description("Registering a new user returns 201 and the created user data")
    public void registerNewUserReturnsCreatedUser() {
        String email = TestData.uniqueEmail();
        String password = TestData.password();

        Response response = api.register(TestData.userName(), email, password);

        Assert.assertEquals(response.getStatusCode(), 201, "registration status");
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "success flag");
        Assert.assertEquals(response.jsonPath().getString("data.email"), email, "registered email");

        String token = api.login(email, password).jsonPath().getString("data.token");
        api.deleteAccount(token);
    }

    @Test
    @Description("Logging in with valid credentials returns 200 and an auth token")
    public void loginReturnsAuthToken() {
        String email = TestData.uniqueEmail();
        String password = TestData.password();
        api.register(TestData.userName(), email, password);

        Response response = api.login(email, password);

        Assert.assertEquals(response.getStatusCode(), 200, "login status");
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "success flag");
        Assert.assertFalse(response.jsonPath().getString("data.token").isBlank(), "token is returned");

        api.deleteAccount(response.jsonPath().getString("data.token"));
    }

    @Test
    @Description("Logging in with a wrong password is rejected")
    public void loginWithWrongPasswordIsRejected() {
        String email = TestData.uniqueEmail();
        String password = TestData.password();
        api.register(TestData.userName(), email, password);

        Response response = api.login(email, password + "X");

        Assert.assertEquals(response.getStatusCode(), 401, "wrong password status");
        Assert.assertFalse(response.jsonPath().getBoolean("success"), "success flag");

        String token = api.login(email, password).jsonPath().getString("data.token");
        api.deleteAccount(token);
    }
}
