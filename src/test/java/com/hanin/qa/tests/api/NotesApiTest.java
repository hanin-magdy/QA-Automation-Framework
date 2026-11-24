package com.hanin.qa.tests.api;

import com.hanin.qa.api.NotesApiClient;
import com.hanin.qa.utils.TestData;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

@Epic("Notes App API")
@Feature("Notes Management")
public class NotesApiTest {

    private final NotesApiClient api = new NotesApiClient();
    private String token;

    @BeforeClass
    public void createUserAndLogin() {
        String email = TestData.uniqueEmail();
        String password = TestData.password();
        Response registered = api.register(TestData.userName(), email, password);
        Assert.assertEquals(registered.getStatusCode(), 201, "setup: user registered");
        token = api.login(email, password).jsonPath().getString("data.token");
        Assert.assertNotNull(token, "setup: auth token");
    }

    @AfterClass(alwaysRun = true)
    public void deleteUser() {
        if (token != null) {
            api.deleteAccount(token);
        }
    }

    @Test
    @Description("Creating a note returns it with the same title, description and category")
    public void createNoteReturnsCreatedNote() {
        String title = TestData.noteTitle();

        Response response = api.createNote(token, title, TestData.noteDescription(), "Work");

        Assert.assertEquals(response.getStatusCode(), 200, "create status");
        Assert.assertEquals(response.jsonPath().getString("data.title"), title, "note title");
        Assert.assertEquals(response.jsonPath().getString("data.category"), "Work", "note category");
        Assert.assertFalse(response.jsonPath().getBoolean("data.completed"), "new note is not completed");

        api.deleteNote(token, response.jsonPath().getString("data.id"));
    }

    @Test
    @Description("A created note shows up in the user's notes list")
    public void createdNoteAppearsInNotesList() {
        String title = TestData.noteTitle();
        String noteId = api.createNote(token, title, TestData.noteDescription(), "Home")
                .jsonPath().getString("data.id");

        List<Map<String, Object>> notes = api.getAllNotes(token).jsonPath().getList("data");
        boolean found = notes.stream().anyMatch(n -> noteId.equals(n.get("id")) && title.equals(n.get("title")));

        Assert.assertTrue(found, "created note is present in GET /notes");
        api.deleteNote(token, noteId);
    }

    @Test
    @Description("A note can be marked as completed")
    public void noteCanBeMarkedCompleted() {
        String title = TestData.noteTitle();
        String noteId = api.createNote(token, title, TestData.noteDescription(), "Personal")
                .jsonPath().getString("data.id");

        Response updated = api.updateNote(token, noteId, title, TestData.noteDescription(), "Personal", true);

        Assert.assertEquals(updated.getStatusCode(), 200, "update status");
        Assert.assertTrue(updated.jsonPath().getBoolean("data.completed"), "note is completed");
        api.deleteNote(token, noteId);
    }

    @Test
    @Description("Deleting a note removes it from the notes list")
    public void deletedNoteDisappearsFromList() {
        String noteId = api.createNote(token, TestData.noteTitle(), TestData.noteDescription(), "Home")
                .jsonPath().getString("data.id");

        Response deleted = api.deleteNote(token, noteId);
        Assert.assertEquals(deleted.getStatusCode(), 200, "delete status");

        Response fetched = api.getNote(token, noteId);
        Assert.assertEquals(fetched.getStatusCode(), 404, "deleted note no longer exists");
    }
}
