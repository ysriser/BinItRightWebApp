package tech3.binitright.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class MainPage {

    private final WebDriver driver;
    private final LoginPage loginPage;
    private final DashboardPage dashboardPage;

    // Combine everything into one constructor
    public MainPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);

        this.loginPage = new LoginPage(driver);
        this.dashboardPage = new DashboardPage(driver);
    }

    public void performLogin(String user, String pass) {
        loginPage.login(user, pass);
    }

    public void performLogout() {
        dashboardPage.clickLogout();
    }
}