package com.hanin.qa.tests.ui;

import com.hanin.qa.api.NotesApiClient;
import com.hanin.qa.base.BaseUiTest;
import com.hanin.qa.pages.LoginPage;
import com.hanin.qa.pages.NotesPage;
import com.hanin.qa.utils.TestData;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Notes App UI")
@Feature("Notes Management")
public class NotesUiTest extends BaseUiTest {

    @Test
    @Description("A logged-in user can create a note through the UI and sees it in the list")
    public void userCanCreateNoteThroughUi() {
        NotesApiClient api = new NotesApiClient();
        String email = TestData.uniqueEmail();
        String password = TestData.password();
        api.register(TestData.userName(), email, password);

        String title = TestData.noteTitle();
        NotesPage notesPage = new LoginPage(driver(), waitFor()).open().login(email, password);
        notesPage.addNote(title, TestData.noteDescription(), "Home");

        Assert.assertTrue(notesPage.isNoteVisible(title), "created note card is visible in the list");
        Assert.assertEquals(notesPage.visibleNoteCount(), 1, "exactly one note exists");

        // cleanup through the API
        String token = api.login(email, password).jsonPath().getString("data.token");
        api.deleteAccount(token);
    }
}
