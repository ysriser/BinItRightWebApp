package techthree.binitright.ui;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import techthree.binitright.pages.MainPage;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckInReview extends Base {

    @Test
    void adminShouldBeAbleToApproveCheckIn() {
        MainPage mainPage = new MainPage(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Login and navigate
        driver.get(baseUrl + "/login");
        mainPage.loginPage.login("admin", "password");
        mainPage.goToCheckInReview();

        // Approve the first item (Index 0)
        mainPage.checkInReviewPage.clickReviewButton(1);
        mainPage.checkInReviewPage.approveCheckIn();

        // 3. Verify success
        wait.until(ExpectedConditions.urlContains("/admin/checkin"));
        assertTrue(driver.getPageSource().contains("APPROVED"), "Approval failed.");
    }

    @Test
    void adminShouldBeAbleToDenyCheckIn() {
        MainPage mainPage = new MainPage(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // 1. Login and navigate
        driver.get(baseUrl + "/login");
        mainPage.loginPage.login("admin", "password");
        mainPage.goToCheckInReview();

        // 2. Deny the second item (Index 1)
        // Note: Using index 1 ensures we are testing a different record
        mainPage.checkInReviewPage.clickReviewButton(2);
        mainPage.checkInReviewPage.denyCheckIn();

        // 3. Verify success
        wait.until(ExpectedConditions.urlContains("/admin/checkin"));
        assertTrue(driver.getPageSource().contains("DENIED"), "Denial failed.");
    }
}