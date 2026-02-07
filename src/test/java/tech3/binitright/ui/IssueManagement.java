package tech3.binitright.ui;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tech3.binitright.pages.MainPage;
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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // Increased timeout

        driver.manage().deleteAllCookies();
        driver.get(baseUrl + "/login");
        mainPage.loginPage.login("admin", "password");

        // 1. Go to the issue
        driver.get(baseUrl + "/admin/issues/" + targetId);

        // 2. CRITICAL: Wait for the status to actually BE In Progress
        // This handles the DB lag between the two test methods.
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector(".status-badge"), "IN_PROGRESS"));

        // 3. Now perform the resolution
        mainPage.issueManagementPage.clickResolveIssue();

        // 4. Verify the final state
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector(".status-badge"), "RESOLVED"));

        assertTrue(driver.getPageSource().contains("RESOLVED"));
    }
}