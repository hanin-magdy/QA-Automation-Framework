package com.hanin.qa.listeners;

import com.hanin.qa.base.BaseUiTest;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

/** Attaches a screenshot to the Allure report whenever a UI test fails. */
public class ScreenshotListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = BaseUiTest.currentDriver();
        if (driver != null) {
            saveScreenshot(driver);
        }
    }

    @Attachment(value = "Screenshot on failure", type = "image/png")
    private byte[] saveScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
