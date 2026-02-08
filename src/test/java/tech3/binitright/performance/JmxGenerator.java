package tech3.binitright.performance;
import static us.abstracta.jmeter.javadsl.JmeterDsl.httpCookies;
import static us.abstracta.jmeter.javadsl.JmeterDsl.httpSampler;
import static us.abstracta.jmeter.javadsl.JmeterDsl.jsonExtractor;
import static us.abstracta.jmeter.javadsl.JmeterDsl.regexExtractor;
import static us.abstracta.jmeter.javadsl.JmeterDsl.testPlan;
import static us.abstracta.jmeter.javadsl.JmeterDsl.threadGroup;

import java.io.IOException;
import java.time.Duration;

import org.apache.http.entity.ContentType;

public class JmxGenerator {
    public static void main(final String[] args) throws IOException {


        final String host = System.getProperty("targetUhost", "localhost");
        final String testUrl = System.getProperty("testUurl");
        String baseUrl;
        if (host.equals("localhost")) {
            // Local environment uses HTTP and 8080
            baseUrl = "http://localhost:8080";
        } else {
            // Remote environments use HTTPS and the provided host
            // We use port 443 (standard for HTTPS)
            baseUrl = "https://" + testUrl;
        }
        final String defaultUser = System.getProperty("perfUuser", "admin");
        final String defaultPass = System.getProperty("perfUpass", "none");
        final String defaultAppUser = System.getProperty("perfUappUuser", "user");
        final String defaultAppPass = System.getProperty("perfUappUpass", "none");


        testPlan(
            threadGroup("AdminULoadUTest")
                .rampTo(5, Duration.ofSeconds(10))
                .holdIterating(10)
                .children(
                    httpCookies(), // Necessary to maintain the session

                    // 1. LOGIN (Required to access /admin/**)
                    httpSampler("1_GETULogin", baseUrl + "/login")
                        .children(
                            regexExtractor("csrfUtoken", "name=\"_csrf\" value=\"(.+?)\"")
                        ),

                    httpSampler("2_POSTULogin", baseUrl + "/login")
                        .method("POST")
                        .contentType(ContentType.APPLICATION_FORM_URLENCODED)
                        .param("username", defaultUser)
                        .param("password", defaultPass)
                        .param("_csrf", "${csrfUtoken}"),

                    httpSampler("GETUAdminUForecast", baseUrl + "/admin/forecast"),
                    httpSampler("4_GETUCheckinUList", baseUrl + "/admin/checkin"),
                    httpSampler("5_GETUReports", baseUrl + "/admin/sustainability-reports")),
                threadGroup("ANDROIDUAPIULoadUTest")
                        .rampTo(10, Duration.ofSeconds(15))
                        .holdIterating(20)
                        .children(

                                httpSampler("APIULogin", baseUrl + "/api/auth/login")
                                        .method("POST")
                                        .contentType(ContentType.APPLICATION_JSON)
                                        .body(String.format(
                                                "{\"username\":\"%s\",\"password\":\"%s\"}",
                                                defaultAppUser, defaultAppPass
                                        ))
                                        .children(
                                                jsonExtractor("extractedUtoken", "token")
                                        ),

                                httpSampler("Access Summary Profile API", baseUrl + "/api/summary/profile")
                                        .header("Authorization", "Bearer ${extractedUtoken}"),

                                httpSampler("Access User Accessories API", baseUrl + "/api/user-accessories/my-items")
                                        .header("Authorization", "Bearer ${extractedUtoken}"),

                                httpSampler("Access News API", baseUrl + "/api/news")
                                        .header("Authorization", "Bearer ${extractedUtoken}"),

                                httpSampler("Access Event API", baseUrl + "/api/events")
                                        .header("Authorization", "Bearer ${extractedUtoken}"),

                                httpSampler("Access Recycle History API", baseUrl + "/api/recycle-history")
                                        .header("Authorization", "Bearer ${extractedUtoken}")
                        )

        ).saveAsJmx("tests/loadUtest.jmx");
    }
}




