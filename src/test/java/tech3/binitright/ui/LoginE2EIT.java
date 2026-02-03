package tech3.binitright.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tech3.binitright.pages.MainPage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginE2EIT {

    private WebDriver driver;
    private MainPage mainPage;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        }

        this.driver = new ChromeDriver(options);

        // Initialize Page Object manually
        this.mainPage = new MainPage(driver);

        // Determine target URL
        // If your CI/CD provides a target_host property, use it; otherwise default to localhost
        String host = System.getProperty("target_host", "localhost");
        String port = System.getProperty("target_port", "8080");
        this.baseUrl = "http://" + host + ":" + port;
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void userShouldLoginAndLogoutSuccessfully() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Navigate to the deployment URL
            driver.get(baseUrl + "/login");

            // Perform Login using Page Object
            mainPage.performLogin("admin", "password");

            // Wait for Dashboard (Fixed typo in your original check: /admin/dashboard)
            wait.until(ExpectedConditions.urlContains("/admin/dashboard"));
            assertTrue(driver.getCurrentUrl().contains("/admin/dashboard"),
                    "Login failed! Current URL: " + driver.getCurrentUrl());

            // Perform Logout
            mainPage.performLogout();

            // Wait for Login screen
            wait.until(ExpectedConditions.urlContains("/login"));
            assertTrue(driver.getCurrentUrl().contains("/login"),
                    "Logout failed! Current URL: " + driver.getCurrentUrl());

        } catch (Exception | AssertionError e) {
            captureAllArtifacts("login_test_failure");
            throw e;
        }
    }

    // --- HELPER METHODS FOR ARTIFACTS ---

    private void captureAllArtifacts(String name) {
        if (driver != null) {
            saveScreenshot(name);
            savePageSource(name);
        }
    }

    @Attachment(value = "{name} Screenshot", type = "image/png")
    public byte[] saveScreenshot(String name) {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
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
        try {
            FileUtils.writeStringToFile(new File("target/screenshots/" + name + ".html"), html, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }
}