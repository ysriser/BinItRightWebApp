package tech3.binitright.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.springframework.stereotype.Component;

@Component
public class MainPage {

    private final LoginPage loginPage;
    private final DashboardPage dashboardPage;

    public MainPage(LoginPage loginPage, DashboardPage dashboardPage) {
        this.loginPage = loginPage;
        this.dashboardPage = dashboardPage;
    }

    public void performLogin(String user, String pass) {
        loginPage.login(user, pass);
    }

    public void performLogout() {
        dashboardPage.clickLogout();
    }
}