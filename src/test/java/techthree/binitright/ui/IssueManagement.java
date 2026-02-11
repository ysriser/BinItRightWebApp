package techthree.binitright.ui;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import techthree.binitright.pages.MainPage;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IssueManagement extends Base {

    private static String targetId;

    @Test
    @Order(1)
    void adminShouldMarkNewIssueAsInProgress() {
        MainPage mainPage = new MainPage(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get(baseUrl + "/login");
        mainPage.loginPage.login("admin", "password");
        mainPage.goToIssueManagement();

        int index = mainPage.issueManagementPage.getFirstIndexByStatus("NEW");
        targetId = mainPage.issueManagementPage.getIssueId(index);

        mainPage.issueManagementPage.clickViewIssue(index);
        mainPage.issueManagementPage.clickMarkInProgress();

        driver.get(baseUrl + "/admin/issues/" + targetId);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector(".review-card .status-badge"), "IN_PROGRESS"));

        assertTrue(mainPage.issueManagementPage.getDetailPageStatus().contains("IN_PROGRESS"));
    }

    @Test
    @Order(2)
    void adminShouldResolveThatSameIssue() {
        Assumptions.assumeTrue(targetId != null, "targetId not set");
        MainPage mainPage = new MainPage(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Explicitly Logout to ensure a clean server-side session
        driver.get(baseUrl + "/logout");

        // Go to login and wait for the username field to be ready
        driver.get(baseUrl + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));

        mainPage.loginPage.login("admin", "password");

        // Wait for the dashboard/management redirect to finish
        wait.until(ExpectedConditions.urlContains("/admin"));

        // Navigate directly to the specific issue
        driver.get(baseUrl + "/admin/issues/" + targetId);

        // NAVIGATION GUARD: Wait for the review card to exist
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".review-card")));

        // STATE GUARD: Wait for the badge to show IN_PROGRESS
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector(".status-badge"), "IN_PROGRESS"));

        // Action & Final Verify
        mainPage.issueManagementPage.clickResolveIssue();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector(".status-badge"), "RESOLVED"));

        assertTrue(driver.getPageSource().contains("RESOLVED"));
    }
}