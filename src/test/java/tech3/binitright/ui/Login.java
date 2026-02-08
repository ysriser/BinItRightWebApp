package tech3.binitright.ui;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tech3.binitright.pages.MainPage;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Login extends Base {

    @Test
    void userShouldLoginAndLogoutSuccessfully() {
        // Initialize the central hub
        MainPage mainPage = new MainPage(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Start at the login page
            driver.get(baseUrl + "/login");

            // Use the loginPage instance inside the hub
            mainPage.loginPage.login("admin", "password");

            // Verify successful entry into the Admin Dashboard
            wait.until(ExpectedConditions.urlContains("/admin/dashboard"));
            assertTrue(driver.getCurrentUrl().contains("/admin/dashboard"),
                    "Login failed! Did not reach the dashboard.");

            // Logout using the dashboardPage instance inside the hub
            mainPage.dashboardPage.clickLogout();

            // Verify successful return to the login screen
            wait.until(ExpectedConditions.urlContains("/login"));
            assertTrue(driver.getCurrentUrl().contains("/login"),
                    "Logout failed! Did not return to the login page.");

        } catch (Exception | AssertionError e) {
            // Capture evidence if something goes wrong
            captureAllArtifacts("loginUlogoutUfailure");
            throw e;
        }
    }
}