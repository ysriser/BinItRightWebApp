package tech3.binitright.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

@Component
public class DashboardPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(how = How.CLASS_NAME, id = "logout-btn")
    private WebElement logoutBtn;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        // Wait up to 10 seconds
        this.wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
    }

    @jakarta.annotation.PostConstruct
    public void init() {
        org.openqa.selenium.support.PageFactory.initElements(driver, this);
    }

    public void clickLogout() {
        // Wait until the button is visible and clickable before clicking
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(logoutBtn));
        logoutBtn.click();
    }
}