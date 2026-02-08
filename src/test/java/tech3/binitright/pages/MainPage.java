package tech3.binitright.pages;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Sub-pages
    public final LoginPage loginPage;
    public final DashboardPage dashboardPage;
    public final CheckInReviewPage checkInReviewPage;
    public final SustainabilityReportPage sustainabilityReportPage;
    public final IssueManagementPage issueManagementPage;

    // Sidebar Selectors

    @FindBy(linkText = "Dashboard")
    private WebElement dashboardSidebarLink;

    @FindBy(linkText = "Check-In Review")
    private WebElement checkInReviewSidebarLink;

    @FindBy(linkText = "Sustainability Reports")
    private WebElement sustainabilityReportsSidebarLink;

    @FindBy(linkText = "Issue Management")
    private WebElement issueManagementSidebarLink;


    public MainPage(final WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);

        this.loginPage = new LoginPage(driver);
        this.dashboardPage = new DashboardPage(driver);
        this.checkInReviewPage = new CheckInReviewPage(driver);
        this.sustainabilityReportPage = new SustainabilityReportPage(driver);
        this.issueManagementPage = new IssueManagementPage(driver);
    }

    // --- SIDEBAR NAVIGATION METHODS ---
    public void goToCheckInReview() {
        wait.until(ExpectedConditions.elementToBeClickable(checkInReviewSidebarLink));
        checkInReviewSidebarLink.click();
    }

    public void goToDashboard() {
        wait.until(ExpectedConditions.elementToBeClickable(dashboardSidebarLink));
        dashboardSidebarLink.click();
    }

    public void goToSustainabilityReports() {
        wait.until(ExpectedConditions.elementToBeClickable(sustainabilityReportsSidebarLink));
        sustainabilityReportsSidebarLink.click();
    }

    public void goToIssueManagement() {
        wait.until(ExpectedConditions.elementToBeClickable(issueManagementSidebarLink));
        issueManagementSidebarLink.click();
    }
}