package com.hanin.qa.pages;

import com.hanin.qa.config.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage {

    private static final By EMAIL = By.cssSelector("[data-testid='login-email']");
    private static final By PASSWORD = By.cssSelector("[data-testid='login-password']");
    private static final By SUBMIT = By.cssSelector("[data-testid='login-submit']");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public LoginPage open() {
        driver.get(Config.uiBaseUrl() + "/login");
        return this;
    }

    public NotesPage login(String email, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL)).sendKeys(email);
        driver.findElement(PASSWORD).sendKeys(password);
        clickReliably(SUBMIT);
        return new NotesPage(driver, wait);
    }
}
