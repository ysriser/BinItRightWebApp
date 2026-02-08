package tech3.binitright.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import tech3.binitright.pages.MainPage;

public class Dashboard extends Base {

    @Test
    void adminShouldBeAbleToNavigateToReviewsFromDashboardTables() {
        final MainPage mainPage = new MainPage(driver);
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Authenticate and stay on Dashboard
        driver.get(baseUrl + "/login");
        mainPage.loginPage.login("admin", "password");

        // Ensure we are on the dashboard
        wait.until(ExpectedConditions.urlContains("/admin/dashboard"));

        // Test Check-In Review Link (First Row of the Table)
        // This targets the .tbl class in your dashboard HTML
        mainPage.dashboardPage.clickReviewCheckIn(0);

        wait.until(ExpectedConditions.urlContains("/admin/review/"));
        assertTrue(driver.getCurrentUrl().contains("/admin/review/"),
                "Did not navigate to individual Check-In Review page from dashboard table.");

        // Go back to Dashboard to test the Issues section
        driver.get(baseUrl + "/admin/dashboard");

        // Test User Issue Review Link (First Card in the Issues section)
        // This targets the .issue-card class in your dashboard HTML
        mainPage.dashboardPage.clickReviewIssue(0);

        wait.until(ExpectedConditions.urlContains("/admin/issues"));
        assertTrue(driver.getCurrentUrl().contains("/admin/issues"),
                "Did not navigate to Issue Management page from dashboard issue card.");
    }
}