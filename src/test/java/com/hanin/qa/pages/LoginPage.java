package com.hanin.qa.pages;

import com.hanin.qa.config.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private static final By EMAIL = By.cssSelector("[data-testid='login-email']");
    private static final By PASSWORD = By.cssSelector("[data-testid='login-password']");
    private static final By SUBMIT = By.cssSelector("[data-testid='login-submit']");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public LoginPage open() {
        driver.get(Config.uiBaseUrl() + "/login");
        return this;
    }

    public NotesPage login(String email, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL)).sendKeys(email);
        driver.findElement(PASSWORD).sendKeys(password);
        driver.findElement(SUBMIT).click();
        return new NotesPage(driver, wait);
    }
}
