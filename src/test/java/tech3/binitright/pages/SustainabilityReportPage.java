package tech3.binitright.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class SustainabilityReportPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Report filter elements
    @FindBy(xpath = "//button[contains(., 'Generate')]")
    private WebElement generateReportBtn;

    @FindBy(linkText = "Back to Reports")
    private WebElement backToReportsBtn;

    // Report archives table
    private final By reportRows = By.cssSelector(".tbl tbody tr");
    private final By viewBtn = By.linkText("View");
    private final By downloadBtn = By.cssSelector("a[title='Download CSV']");

    public SustainabilityReportPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void clickGenerate() {
        wait.until(ExpectedConditions.elementToBeClickable(generateReportBtn));
        generateReportBtn.click();
    }

    public void clickLastReportView() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(reportRows));
        List<WebElement> rows = driver.findElements(reportRows);

        if (!rows.isEmpty()) {
            // Access the last row in the list
            WebElement lastRow = rows.get(rows.size() - 1);
            lastRow.findElement(viewBtn).click();
        }
    }

    public void clickLastReportDownload() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(reportRows));
        List<WebElement> rows = driver.findElements(reportRows);

        if (!rows.isEmpty()) {
            // Access the last row in the list
            WebElement lastRow = rows.get(rows.size() - 1);
            lastRow.findElement(downloadBtn).click();
        }
    }

    public void clickBackToReports() {
        wait.until(ExpectedConditions.elementToBeClickable(backToReportsBtn));
        backToReportsBtn.click();
    }
}