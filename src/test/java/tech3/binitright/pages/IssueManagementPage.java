package tech3.binitright.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class IssueManagementPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By issueCards = By.cssSelector(".review-card");
    private final By viewIssueLink = By.linkText("View Issue");
    private final By statusBadge = By.className("status-badge");

    public IssueManagementPage(final WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public int getFirstIndexByStatus(final String targetStatus) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(issueCards));
        final List<WebElement> cards = driver.findElements(issueCards);
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).findElement(statusBadge).getText().trim().equalsIgnoreCase(targetStatus)) {
                return i;
            }
        }
        throw new RuntimeException("No issues found with status: " + targetStatus);
    }

    public String getIssueId(final int index) {
        final List<WebElement> cards = driver.findElements(issueCards);
        final String href = cards.get(index).findElement(viewIssueLink).getAttribute("href");
        return href.substring(href.lastIndexOf("/") + 1);
    }

    public void clickViewIssue(final int index) {
        final List<WebElement> cards = driver.findElements(issueCards);
        cards.get(index).findElement(viewIssueLink).click();
    }

    public void clickMarkInProgress() {
        final By locator = By.xpath("//button[contains(., 'In Progress')]");
        final WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(locator));
        btn.click();
        wait.until(ExpectedConditions.stalenessOf(btn));
    }

    public void clickResolveIssue() {
        final By locator = By.xpath("//button[contains(., 'Resolve')]");

        for (int i = 0; i < 3; i++) {
            try {
                final WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(locator));

                // Fix for Mac/Headless: Scroll the button to the middle of the screen
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);

                btn.click();
                wait.until(ExpectedConditions.stalenessOf(btn));
                return;
            } catch (final Exception e) {
                System.out.println(">>> Resolve attempt " + (i + 1) + " failed. Refreshing...");
                driver.navigate().refresh();
                try { Thread.sleep(1000); } catch (final InterruptedException ignored) {}
            }
        }
        throw new RuntimeException("Resolve button never appeared or was blocked.");
    }

    public String getDetailPageStatus() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".review-card .status-badge"))).getText();
    }
}