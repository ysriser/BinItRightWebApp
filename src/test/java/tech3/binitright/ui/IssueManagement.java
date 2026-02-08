package tech3.binitright.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import tech3.binitright.pages.MainPage;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IssueManagement extends Base {

    private static String targetId;

    @Test
    @Order(1)
    void adminShouldMarkNewIssueAsInProgress() {
        final MainPage mainPage = new MainPage(driver);
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get(baseUrl + "/login");
        mainPage.loginPage.login("admin", "password");
        mainPage.goToIssueManagement();

        final int index = mainPage.issueManagementPage.getFirstIndexByStatus("NEW");
        targetId = mainPage.issueManagementPage.getIssueId(index);

        mainPage.issueManagementPage.clickViewIssue(index);
        mainPage.issueManagementPage.clickMarkInProgress();

        driver.get(baseUrl + "/admin/issues/" + targetId);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector(".review-card .status-badge"), "INUPROGRESS"));

        assertTrue(mainPage.issueManagementPage.getDetailPageStatus().contains("INUPROGRESS"));
    }

    @Test
    @Order(2)
    void adminShouldResolveThatSameIssue() {
        Assumptions.assumeTrue(targetId != null, "targetId not set");
        final MainPage mainPage = new MainPage(driver);
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

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

        // STATE GUARD: Wait for the badge to show INUPROGRESS
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector(".status-badge"), "INUPROGRESS"));

        // Action & Final Verify
        mainPage.issueManagementPage.clickResolveIssue();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector(".status-badge"), "RESOLVED"));

        assertTrue(driver.getPageSource().contains("RESOLVED"));
    }
}