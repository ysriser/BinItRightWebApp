package tech3.binitright.performance; // Ensure this matches your folder structure

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;
import java.io.IOException;

public class JmxGenerator {
    public static void main(String[] args) throws IOException {
        // We read the property in Java first to ensure the type is correct
        int port = Integer.parseInt(System.getProperty("target_port", "80"));

        testPlan(
            threadGroup("CI_Load_Test", 10, 100,
                httpSampler("Homepage", "${__P(target_host, localhost)}")
                    .port(port) // Now passing a valid integer
                    .method("GET")
            )
        ).saveAsJmx("tests/load_test.jmx");
        
        System.out.println("JMX file generated successfully in /tests folder.");
    }
}
