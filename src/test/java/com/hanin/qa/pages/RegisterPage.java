package com.hanin.qa.pages;

import com.hanin.qa.config.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RegisterPage extends BasePage {

    private static final By EMAIL = By.cssSelector("[data-testid='register-email']");
    private static final By NAME = By.cssSelector("[data-testid='register-name']");
    private static final By PASSWORD = By.cssSelector("[data-testid='register-password']");
    private static final By CONFIRM_PASSWORD = By.cssSelector("[data-testid='register-confirm-password']");
    private static final By SUBMIT = By.cssSelector("[data-testid='register-submit']");
    private static final By SUCCESS_MESSAGE = By.xpath("//*[contains(text(),'User account created successfully')]");

    public RegisterPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public RegisterPage open() {
        driver.get(Config.uiBaseUrl() + "/register");
        return this;
    }

    public RegisterPage register(String name, String email, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL)).sendKeys(email);
        driver.findElement(NAME).sendKeys(name);
        driver.findElement(PASSWORD).sendKeys(password);
        driver.findElement(CONFIRM_PASSWORD).sendKeys(password);
        clickReliably(SUBMIT);
        return this;
    }

    public boolean isSuccessMessageShown() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(SUCCESS_MESSAGE)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
