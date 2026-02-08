package tech3.binitright.performance;

import static us.abstracta.jmeter.javadsl.JmeterDsl.httpSampler;
import static us.abstracta.jmeter.javadsl.JmeterDsl.jsonExtractor;
import static us.abstracta.jmeter.javadsl.JmeterDsl.resultsTreeVisualizer;
import static us.abstracta.jmeter.javadsl.JmeterDsl.testPlan;
import static us.abstracta.jmeter.javadsl.JmeterDsl.threadGroup;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class JmxGeneratorSampleHttps {

    @Test
    public void runPerformanceTest() throws IOException {
        testPlan(
                threadGroup(1, 1,
                        // STEP 1: LOGIN (The gatekeeper)
                        httpSampler("Login Request", "https://test.binitright.app/api/auth/login")
                                .method("POST")
                                .contentType(org.apache.http.entity.ContentType.APPLICATION_JSON)
                                .body("{\"username\": \"User1\", \"password\": \"password\"}")
                                .children(
                                        jsonExtractor("extractedUtoken", "token")
                                ),

                        // STEP 2: PROTECTED RESOURCE (The test)
                        httpSampler("Access Summary Profile API", "https://test.binitright.app/api/summary/profile")
                                .header("Authorization", "Bearer ${extractedUtoken}")
                )
                // comment out below resultTreeVisualizer to disable GUI
                , resultsTreeVisualizer()
        ).run();
    }
}
