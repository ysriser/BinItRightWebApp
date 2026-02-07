package tech3.binitright.performance;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;
import java.io.IOException;
import java.time.Duration;

public class JmxGenerator {
    public static void main(String[] args) throws IOException {
        String host = "${__P(target_host, localhost)}";
        int port = Integer.parseInt(System.getProperty("target_port", "8080"));
        String baseUrl = "http://" + host + ":" + port;
        String defaultUser = System.getProperty("perf_user"); 
        String defaultPass = System.getProperty("perf_pass");

        testPlan(
            threadGroup("Web_Admin_Load_Test")
                .rampTo(5, Duration.ofSeconds(10))
                .holdIterating(10)
                .children(
                    //  Manages JSESSIONID automatically
                    httpCookieManager(),
                
                    //  GET the login page to "scrape" the CSRF token
                    httpSampler("1_GET_Login_Page", baseUrl + "/login")
                        .children(
                            // This looks for the hidden input field Spring adds to the form
                            regexExtractor("csrf_token", "name=\"_csrf\" value=\"(.+?)\"")
                        ),

                    //  POST Login - Spring security requires username, password, and _csrf
                    httpSampler("2_POST_Login_Submit", baseUrl + "/login")
                        .method("POST")
                        .contentType(ContentType.APPLICATION_FORM_URLENCODED)
                        .param("username", defaultUser)
                        .param("password",defaultPass)
                        .param("_csrf", "${csrf_token}"), 
                    
                    httpSampler("3_Admin_Dashboard", baseUrl + "/admin/dashboard")
                        .children(
                            responseAssertion().containsSubstrings("Dashboard")
                        ),

                    httpSampler("4_Sustainability_Report", baseUrl + "/admin/sustainability-reports"),

                    httpSampler("5_Issue_Management", baseUrl + "/admin/issues")
                )
        ).saveAsJmx("tests/load_test.jmx");

        System.out.println("JMX Generated.");
    }
}
