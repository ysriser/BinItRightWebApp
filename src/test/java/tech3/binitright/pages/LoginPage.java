package tech3.binitright.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.springframework.stereotype.Component;

@Component
public class LoginPage {
    private final WebDriver driver;

    @FindBy(how = How.NAME, using = "username")
    public WebElement usernameField;

    @FindBy(how = How.NAME, using = "password")
    public WebElement passwordField;

    @FindBy(how = How.CLASS_NAME, className = "login-btn")
    private WebElement loginBtn;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    @jakarta.annotation.PostConstruct
    public void init() {
        org.openqa.selenium.support.PageFactory.initElements(driver, this);
    }

    public void login(String username, String password) {
        usernameField.clear();
        usernameField.sendKeys(username);
        passwordField.clear();
        passwordField.sendKeys(password);
        loginBtn.click();
    }
}