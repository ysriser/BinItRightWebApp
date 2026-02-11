package techthree.binitright.performance;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

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
                                        jsonExtractor("extracted_token", "token")
                                ),

                        // STEP 2: PROTECTED RESOURCE (The test)
                        httpSampler("Access Summary Profile API", "https://test.binitright.app/api/summary/profile")
                                .header("Authorization", "Bearer ${extracted_token}")
                )
                // comment out below resultTreeVisualizer to disable GUI
                , resultsTreeVisualizer()
        ).run();
    }
}
