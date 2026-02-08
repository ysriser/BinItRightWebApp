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

public class CheckInReviewPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // List Page Elements
    private final By checkInRows = By.cssSelector(".tbl tbody tr");
    private final By reviewLink = By.className("btn-review");

    // Detail Review Page Elements
    @FindBy(className = "btn-approve")
    private WebElement approveBtn;

    @FindBy(className = "btn-deny")
    private WebElement denyBtn;

    public CheckInReviewPage(final WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // Navigate to a specific review from the list
    public void clickReviewButton(final int index) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(checkInRows));
        final List<WebElement> rows = driver.findElements(checkInRows);
        rows.get(index).findElement(reviewLink).click();
    }

    public void approveCheckIn() {
        wait.until(ExpectedConditions.elementToBeClickable(approveBtn));
        approveBtn.click();
    }

    public void denyCheckIn() {
        wait.until(ExpectedConditions.elementToBeClickable(denyBtn));
        denyBtn.click();
    }
}