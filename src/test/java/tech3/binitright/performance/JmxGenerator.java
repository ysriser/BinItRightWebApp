package tech3.binitright.performance;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;
import java.io.IOException;
import java.time.Duration;
import org.apache.http.entity.ContentType;

public class JmxGenerator {
    public static void main(String[] args) throws IOException {


        String host = System.getProperty("target_host", "localhost");
        String testUrl = System.getProperty("test_url");
        String baseUrl;
        if (host.equals("localhost")) {
            // Local environment uses HTTP and 8080
            baseUrl = "http://localhost:8080";
        } else {
            // Remote environments use HTTPS and the provided host
            // We use port 443 (standard for HTTPS)
            baseUrl = "https://" + testUrl;
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
                        
                    httpSampler("GET_Admin_Forecast", baseUrl + "/admin/forecast"),

                    httpSampler("4_GET_Checkin_List", baseUrl + "/admin/checkin"),
                    
                     
                    httpSampler("5_GET_Reports", baseUrl + "/admin/sustainability-reports")),

            threadGroup("ANDROID_API_Load_Test")
                .rampTo(10, Duration.ofSeconds(15))
                .holdIterating(20)
                .children(
                    httpSampler("API_Login", baseUrl + "/api/auth/login")
                        .method("POST")
                        .contentType(ContentType.APPLICATION_JSON)
                        .body("""
                            {
                              "username": "androiduser",
                              "password": "password"
                            }
                        """)
                        .children(
                            jsonExtractor("jwt_token", "token")
                        ),

                    httpHeaders()
                        .header("Authorization", "Bearer ${jwt_token}")
                        .header("Content-Type", "application/json"),

                    httpSampler("API_GET_News_List",
                            baseUrl + "/api/news"),

                    httpSampler("API_GET_News_By_Id",
                            baseUrl + "/api/news/1"),

                    httpSampler("API_GET_Events_All",
                            baseUrl + "/api/events"),

                    httpSampler("API_GET_Events_Upcoming",
                            baseUrl + "/api/events?filter=upcoming")
                )
                
        ).saveAsJmx("tests/load_test.jmx");
        
        System.out.println("JMX Generated for: " + baseUrl);
    }
}
