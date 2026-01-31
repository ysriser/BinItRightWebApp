package com.binitright.performance;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;
import java.io.IOException;

public class JmxGenerator {
    public static void main(String[] args) throws IOException {
        // Define your test plan programmatically
        testPlan(
            threadGroup("CI_Load_Test", 10, 100, // 10 users, 100 iterations
                httpSampler("Homepage", "${__P(target_host, localhost)}")
                    .port("${__P(target_port, 80)}")
                    .method("GET")
            )
        ).saveAsJmx("tests/load_test.jmx"); // Saves to project root/tests/
        
        System.out.println("JMX file generated successfully in /tests folder.");
    }
}
