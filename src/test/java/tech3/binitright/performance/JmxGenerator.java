package tech3.binitright.performance;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;
import java.io.IOException;
import java.time.Duration;

public class JmxGenerator {
    public static void main(String[] args) throws IOException {
        String host = "${__P(target_host, localhost)}";
        int port = Integer.parseInt(System.getProperty("target_port", "8080"));
        String baseUrl = "http://" + host + ":" + port;

        testPlan(
            threadGroup("Web_Admin_Load_Test")
                .rampTo(5, Duration.ofSeconds(10))
                .holdIterating(10)
                .children(
                    // 1. Manages JSESSIONID automatically
                    httpCookieManager(),
                    
                    // 2. Load admin users from your CSV
                    csvDataSet("src/test/resources/admin_users.csv")
                        .variableNames("admin_user", "admin_password"),

                    // 3. GET the login page to "scrape" the CSRF token
                    httpSampler("1_GET_Login_Page", baseUrl + "/login")
                        .children(
                            // This looks for the hidden input field Spring adds to the form
                            regexExtractor("csrf_token", "name=\"_csrf\" value=\"(.+?)\"")
                        ),

                    // 4. POST Login - Spring security requires username, password, and _csrf
                    httpSampler("2_POST_Login_Submit", baseUrl + "/login")
                        .method("POST")
                        .contentType(ContentType.APPLICATION_FORM_URLENCODED)
                        .param("username", "${admin_user}")
                        .param("password", "${admin_password}")
                        .param("_csrf", "${csrf_token}"), 
                    
                    httpSampler("3_Admin_Dashboard", baseUrl + "/admin/dashboard")
                        .children(
                            responseAssertion().containsSubstrings("Dashboard")
                        ),

                    httpSampler("4_Sustainability_Report", baseUrl + "/admin/sustainability-reports"),

                    httpSampler("5_Issue_Management", baseUrl + "/admin/issues")
                )
        ).saveAsJmx("tests/web_admin_load_test.jmx");

        System.out.println("JMX Generated. Remember to create src/test/resources/admin_users.csv");
    }
}
