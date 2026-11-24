package com.hanin.qa.api;

import com.hanin.qa.config.Config;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

/**
 * Thin client over the Notes App REST API.
 * Base URL and behaviour verified against the live service.
 */
public class NotesApiClient {

    private final RequestSpecification spec;

    public NotesApiClient() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
        this.spec = new RequestSpecBuilder()
                .setBaseUri(Config.apiBaseUrl())
                .setContentType(ContentType.JSON)
                .build();
    }

    private RequestSpecification withToken(String token) {
        return RestAssured.given(spec).header("x-auth-token", token);
    }

    public Response register(String name, String email, String password) {
        return RestAssured.given(spec).body(Map.of(
                "name", name,
                "email", email,
                "password", password)).post("/users/register");
    }

    public Response login(String email, String password) {
        return RestAssured.given(spec).body(Map.of(
                "email", email,
                "password", password)).post("/users/login");
    }

    public Response createNote(String token, String title, String description, String category) {
        return withToken(token).body(Map.of(
                "title", title,
                "description", description,
                "category", category)).post("/notes");
    }

    public Response getAllNotes(String token) {
        return withToken(token).get("/notes");
    }

    public Response getNote(String token, String noteId) {
        return withToken(token).get("/notes/" + noteId);
    }

    /** The API requires the full note body on update, not only the changed field. */
    public Response updateNote(String token, String noteId, String title, String description, String category, boolean completed) {
        return withToken(token).body(Map.of(
                "title", title,
                "description", description,
                "category", category,
                "completed", completed)).put("/notes/" + noteId);
    }

    public Response deleteNote(String token, String noteId) {
        return withToken(token).delete("/notes/" + noteId);
    }

    public Response deleteAccount(String token) {
        return withToken(token).delete("/users/delete-account");
    }
}
