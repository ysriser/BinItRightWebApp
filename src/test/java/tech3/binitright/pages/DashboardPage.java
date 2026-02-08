package tech3.binitright.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DashboardPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "logout-btn")
    private WebElement logoutBtn;

    public DashboardPage(final WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutBtn));
        logoutBtn.click();
    }

    public void clickReviewCheckIn(final int index) {
        final List<WebElement> rows = driver.findElements(By.cssSelector(".tbl tbody tr"));
        rows.get(index).findElement(By.className("btn-review")).click();
    }

    public void clickReviewIssue(final int index) {
        final List<WebElement> cards = driver.findElements(By.className("issue-card"));
        cards.get(index).findElement(By.className("link-review")).click();
    }
}