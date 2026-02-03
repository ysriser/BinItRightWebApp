package tech3.binitright.ui;

import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import tech3.binitright.pages.MainPage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private MainPage mainPage;

    @Autowired
    private WebDriver driver;

    // This runs after EVERY test method, success or failure
    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit(); // This kills the Chrome process
        }
    }

    @Test
    void userShouldLoginAndLogoutSuccessfully() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Navigate
            driver.get("http://localhost:" + port + "/login");

            // Perform Login
            mainPage.performLogin("admin", "password");

            // Wait for Dashboard
            wait.until(ExpectedConditions.urlContains("/admin/dashboard"));
            assertTrue(driver.getCurrentUrl().contains("/admin/adashboard"), "Login failed!");

            // Perform Logout
            mainPage.performLogout();

            // Wait for Login
            wait.until(ExpectedConditions.urlContains("/login"));
            assertTrue(driver.getCurrentUrl().contains("/login"), "Logout failed!");

        } catch (Exception | AssertionError e) {
            // If anything fails, capture everything
            captureAllArtifacts("login_test_failure");
            throw e; // Still throw the error so the test is marked as 'Failed'
        }
    }

    // --- HELPER METHODS FOR ARTIFACTS ---

    private void captureAllArtifacts(String name) {
        saveScreenshot(name);
        savePageSource(name);
    }

    @Attachment(value = "{name} Screenshot", type = "image/png")
    public byte[] saveScreenshot(String name) {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        // Also save to disk for GitHub Action's raw artifact upload
        try {
            FileUtils.writeByteArrayToFile(new File("target/screenshots/" + name + ".png"), screenshot);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screenshot;
    }

    @Attachment(value = "{name} Page Source", type = "text/html")
    public String savePageSource(String name) {
        String html = driver.getPageSource();
        // Also save to disk for GitHub Action's raw artifact upload
        try {
            FileUtils.writeStringToFile(new File("target/screenshots/" + name + ".html"), html, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }

}