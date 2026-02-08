/*Record 2*/
package tech3.binitright;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the BinItRight application.
 */
@SpringBootApplication
public class BinItRightApplication {

    /**
     * Protected constructor to prevent instantiation.
     */
    protected BinItRightApplication() {
        // Prevent utility class instantiation
    }

    /**
     * Main method to start the Spring Boot application.
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(BinItRightApplication.class, args);
    }
}