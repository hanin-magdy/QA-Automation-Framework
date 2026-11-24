package com.hanin.qa.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Central configuration. Every value can be overridden from the command line,
 * e.g. mvn test -Dbase.url.api=https://example.com/notes/api -Dheadless=false
 */
public final class Config {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                PROPS.load(in);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not load config.properties", e);
        }
    }

    private Config() {
    }

    private static String get(String key) {
        String override = System.getProperty(key);
        if (override != null && !override.isBlank()) {
            return override;
        }
        return PROPS.getProperty(key);
    }

    public static String uiBaseUrl() {
        return get("base.url.ui");
    }

    public static String apiBaseUrl() {
        return get("base.url.api");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public static int timeoutSeconds() {
        return Integer.parseInt(get("timeout.seconds"));
    }
}
