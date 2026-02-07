package tech3.binitright.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class IssueManagementPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By issueCards = By.cssSelector(".review-card");
    private final By viewIssueLink = By.linkText("View Issue");
    private final By statusBadge = By.className("status-badge");

    public IssueManagementPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public int getFirstIndexByStatus(String targetStatus) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(issueCards));
        List<WebElement> cards = driver.findElements(issueCards);
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).findElement(statusBadge).getText().trim().equalsIgnoreCase(targetStatus)) {
                return i;
            }
        }
        throw new RuntimeException("No issues found with status: " + targetStatus);
    }

    public String getIssueId(int index) {
        List<WebElement> cards = driver.findElements(issueCards);
        String href = cards.get(index).findElement(viewIssueLink).getAttribute("href");
        return href.substring(href.lastIndexOf("/") + 1);
    }

    public void clickViewIssue(int index) {
        List<WebElement> cards = driver.findElements(issueCards);
        cards.get(index).findElement(viewIssueLink).click();
    }

    public void clickMarkInProgress() {
        By locator = By.xpath("//button[contains(., 'In Progress')]");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(locator));
        btn.click();
        wait.until(ExpectedConditions.stalenessOf(btn));
    }

    public void clickResolveIssue() {
        // Broaden locator to handle potential icons inside the button
        By locator = By.xpath("//button[contains(., 'Resolve')]");

        for (int i = 0; i < 3; i++) {
            try {
                // Wait for the button to be physically ready for interaction
                WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(locator));

                // Standard click
                btn.click();

                // Wait for the page to transition (staleness)
                wait.until(ExpectedConditions.stalenessOf(btn));
                return;
            } catch (Exception e) {
                System.out.println(">>> Resolve button attempt " + (i + 1) + " failed. Refreshing...");
                driver.navigate().refresh();
                // Brief pause to allow JS/CSS to settle after refresh
                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            }
        }

        // Final debug info if it fails
        String currentBadge = driver.findElement(By.cssSelector(".status-badge")).getText();
        throw new RuntimeException("Resolve button never appeared. Status is: " + currentBadge);
    }

    public String getDetailPageStatus() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".review-card .status-badge"))).getText();
    }
}