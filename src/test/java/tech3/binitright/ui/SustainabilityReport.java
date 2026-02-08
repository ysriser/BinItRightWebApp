package tech3.binitright.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import tech3.binitright.pages.MainPage;

public class SustainabilityReport extends Base {

    @Test
    void adminShouldBeAbleToManageSustainabilityReports() {
        final MainPage mainPage = new MainPage(driver);
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Login and Navigate
        driver.get(baseUrl + "/login");
        mainPage.loginPage.login("admin", "password");
        mainPage.goToSustainabilityReports();

        // Click Generate and wait for the page to refresh/reload
        mainPage.sustainabilityReportPage.clickGenerate();

        // This ensures the new report is actually in the DOM before we look for "Last"
        wait.until(ExpectedConditions.urlContains("/admin/sustainability-reports"));

        // Click View on the LAST report
        mainPage.sustainabilityReportPage.clickLastReportView();

        // Verify View Page
        wait.until(ExpectedConditions.urlContains("/admin/report/view/"));
        assertTrue(driver.getCurrentUrl().contains("/admin/report/view/"),
                "Failed to reach Report View page.");

        // Click Back and WAIT for the list to reappear
        mainPage.sustainabilityReportPage.clickBackToReports();
        wait.until(ExpectedConditions.urlContains("/admin/sustainability-reports"));

        // Download the LAST report
        mainPage.sustainabilityReportPage.clickLastReportDownload();

        // Final Health Check Assertion
        final String currentUrl = driver.getCurrentUrl();
        final String pageSource = driver.getPageSource().toLowerCase();

        assertTrue(currentUrl.contains("/admin/sustainability-reports"),
                "Unexpected navigation after download click: " + currentUrl);

        assertTrue(!pageSource.contains("error 500") && !pageSource.contains("404 not found"),
                "Server error or broken link detected after clicking download!");
    }
}
