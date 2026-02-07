package tech3.binitright.performance;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;
import java.io.IOException;
import java.time.Duration;

public class JmxGenerator {
    public static void main(String[] args) throws IOException {
        String host = System.getProperty("target_host", "localhost");
        int port = Integer.getInteger("target_port", 8080);
        String baseUrl = "http://" + host + ":" + port;
        String defaultUser = System.getProperty("perf_user"); 
        String defaultPass = System.getProperty("perf_pass");

        testPlan(
            threadGroup("Web_Admin_Load_Test")
                .rampTo(5, Duration.ofSeconds(10))
                .holdIterating(10)
                .children(
                    httpCookies(), 
                    httpSampler("1_GET_Login_Page", baseUrl + "/login")
                        .children(
                            regexExtractor("csrf_token", "name=\"_csrf\" value=\"(.+?)\"")
                        ),

                    httpSampler("2_POST_Login_Submit", baseUrl + "/login")
                        .method("POST")
                        .contentType("application/x-www-form-urlencoded")
                        .param("username", defaultUser)
                        .param("password", defaultPass)
                        .param("_csrf", "${csrf_token}"), 
                    
                    httpSampler("3_Admin_Dashboard", baseUrl + "/admin/dashboard")
                        .children(
                            responseAssertion().containsSubstrings("Dashboard")
                        ),

                    httpSampler("4_Sustainability_Report", baseUrl + "/admin/sustainability-reports"),

                    httpSampler("5_Issue_Management", baseUrl + "/admin/issues")
                )
        ).saveAsJmx("tests/load_test.jmx");

        System.out.println("JMX Generated");
    }
}
