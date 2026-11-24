package com.hanin.qa.utils;

import java.time.Instant;

/** Generates unique test data so parallel and repeated runs never collide. */
public final class TestData {

    private TestData() {
    }

    public static String uniqueEmail() {
        return "qa.auto." + Instant.now().toEpochMilli() + "@testmail.dev";
    }

    public static String userName() {
        return "QA Automation";
    }

    public static String password() {
        return "QaPass-" + Instant.now().toEpochMilli();
    }

    public static String noteTitle() {
        return "Automated note " + Instant.now().toEpochMilli();
    }

    public static String noteDescription() {
        return "Created by the automated UI/API framework.";
    }
}
