package tech3.binitright.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class Base {

    protected WebDriver driver;
    protected String baseUrl;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            options.addArguments("--headless=new", "--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");
        }

        this.driver = new ChromeDriver(options);

        String host = System.getProperty("target_host", "localhost");

        if (host.equals("localhost")) {
            // Local environment uses HTTP and 8080
            this.baseUrl = "http://localhost:8080";
        } else {
            // Remote environments use HTTPS and the provided host
            // We use port 443 (standard for HTTPS)
            this.baseUrl = "https://" + host;
        }

        System.out.println(">>> Testing on: " + this.baseUrl);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected void captureAllArtifacts(String name) {
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
        } catch (IOException e) { e.printStackTrace(); }
        return screenshot;
    }

    @Attachment(value = "{name} Page Source", type = "text/html")
    public String savePageSource(String name) {
        String html = driver.getPageSource();
        try {
            FileUtils.writeStringToFile(new File("target/screenshots/" + name + ".html"), html, StandardCharsets.UTF_8);
        } catch (IOException e) { e.printStackTrace(); }
        return html;
    }
}