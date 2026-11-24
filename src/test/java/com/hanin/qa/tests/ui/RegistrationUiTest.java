package com.hanin.qa.tests.ui;

import com.hanin.qa.api.NotesApiClient;
import com.hanin.qa.base.BaseUiTest;
import com.hanin.qa.pages.LoginPage;
import com.hanin.qa.pages.NotesPage;
import com.hanin.qa.pages.RegisterPage;
import com.hanin.qa.utils.TestData;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Notes App UI")
@Feature("User Registration and Login")
public class RegistrationUiTest extends BaseUiTest {

    @Test
    @Description("A new user can register through the UI and then log in")
    public void userCanRegisterAndLoginThroughUi() {
        String email = TestData.uniqueEmail();
        String password = TestData.password();

        RegisterPage registerPage = new RegisterPage(driver(), waitFor()).open();
        registerPage.register(TestData.userName(), email, password);
        Assert.assertTrue(registerPage.isSuccessMessageShown(),
                "success message is shown after registration");

        NotesPage notesPage = new LoginPage(driver(), waitFor()).open().login(email, password);
        Assert.assertTrue(notesPage.isUserLoggedIn(), "user is logged in after UI login");

        // cleanup: remove the account created by this test
        String token = new NotesApiClient().login(email, password).jsonPath().getString("data.token");
        new NotesApiClient().deleteAccount(token);
    }
}
