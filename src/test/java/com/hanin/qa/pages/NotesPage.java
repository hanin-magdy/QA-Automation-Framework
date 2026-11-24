package com.hanin.qa.pages;

import com.hanin.qa.config.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NotesPage extends BasePage {

    private static final By ADD_NOTE = By.cssSelector("[data-testid='add-new-note']");
    private static final By NOTE_TITLE = By.cssSelector("[data-testid='note-title']");
    private static final By NOTE_DESCRIPTION = By.cssSelector("[data-testid='note-description']");
    private static final By NOTE_CATEGORY = By.cssSelector("[data-testid='note-category']");
    private static final By NOTE_SUBMIT = By.cssSelector("[data-testid='note-submit']");
    private static final By LOGOUT = By.cssSelector("[data-testid='logout']");
    private static final By NOTE_CARD_TITLES = By.cssSelector("[data-testid='note-card-title']");

    public NotesPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public NotesPage open() {
        driver.get(Config.uiBaseUrl());
        return this;
    }

    public boolean isUserLoggedIn() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(LOGOUT)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public NotesPage addNote(String title, String description, String category) {
        clickReliably(ADD_NOTE);
        wait.until(ExpectedConditions.visibilityOfElementLocated(NOTE_TITLE)).sendKeys(title);
        driver.findElement(NOTE_DESCRIPTION).sendKeys(description);
        new Select(driver.findElement(NOTE_CATEGORY)).selectByVisibleText(category);
        clickReliably(NOTE_SUBMIT);
        return this;
    }

    public boolean isNoteVisible(String title) {
        By card = By.xpath("//*[@data-testid='note-card-title' and normalize-space()='" + title + "']");
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(card)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public int visibleNoteCount() {
        return driver.findElements(NOTE_CARD_TITLES).size();
    }
}
