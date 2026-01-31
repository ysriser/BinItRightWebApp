package tech3.binitright.performance;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;
import java.io.IOException;
import java.time.Duration;

public class JmxGenerator {
    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getProperty("target_port", "8080"));

        testPlan(
                threadGroup("CI_Load_Test")
                        .rampTo(10, Duration.ofSeconds(30))
                        .holdIterating(20)
                        .children(
                                uniformRandomTimer(Duration.ofSeconds(1), Duration.ofSeconds(3)),
                                httpSampler("Homepage", "${__P(target_host, localhost)}")
                                        .port(port)
                                        .method("GET")
                        )
        ).saveAsJmx("tests/load_test.jmx");

        System.out.println("JMX file generated successfully.");
    }
}