package tech3.binitright.performance;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;
import java.io.IOException;
import java.time.Duration;
import org.apache.http.entity.ContentType;

public class JmxGenerator {
    public static void main(String[] args) throws IOException {
       
        String host = System.getProperty("target_host");
        String testUrl = System.getProperty("test_url");
        String baseUrl;
        
        if (host != null && !host.isEmpty()) {
            // target_host is set, check if test_url is also provided
            if (testUrl != null && !testUrl.isEmpty()) {
                // Use the full test_url and add https protocol
                baseUrl = "https://" + testUrl;
            } else {
                // Build URL from target_host and target_port
                int port = Integer.getInteger("target_port", 8080);
                baseUrl = "http://" + host + ":" + port;
            }
        } else {
            // No target_host, use default localhost
            baseUrl = "http://localhost:8080";
        }
        
        String defaultUser = System.getProperty("perf_user", "admin");
        String defaultPass = System.getProperty("perf_pass", "none");
        
        testPlan(
            threadGroup("Admin_Load_Test")
                .rampTo(5, Duration.ofSeconds(10))
                .holdIterating(10)
                .children(
                    httpCookies(), // Necessary to maintain the session
                    
                    // 1. LOGIN (Required to access /admin/**)
                    httpSampler("1_GET_Login", baseUrl + "/login")
                        .children(
                            regexExtractor("csrf_token", "name=\"_csrf\" value=\"(.+?)\"")
                        ),
                        
                    httpSampler("2_POST_Login", baseUrl + "/login")
                        .method("POST")
                        .contentType(ContentType.APPLICATION_FORM_URLENCODED)
                        .param("username", defaultUser)
                        .param("password", defaultPass)
                        .param("_csrf", "${csrf_token}"),
                        
                    // 3. ADMIN DASHBOARD 
                    httpSampler("3_GET_Admin_Dashboard", baseUrl + "/admin/dashboard")
                        .children(
                            responseAssertion().containsSubstrings("admin-dashboard")
                        ),
                        
                    // 4. CHECK-IN LIST
                    httpSampler("4_GET_Checkin_List", baseUrl + "/admin/checkin"),
                    
                    // 5. SUSTAINABILITY REPORTS 
                    httpSampler("5_GET_Reports", baseUrl + "/admin/sustainability-reports")
                )
        ).saveAsJmx("tests/load_test.jmx");
        
        System.out.println("JMX Generated for: " + baseUrl);
    }
}
