package techthree.binitright.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import techthree.binitright.service.BinDataImporter;

import java.util.logging.Logger;

@Configuration
public class BinSeeder {

    Logger logger = Logger.getLogger(getClass().getName());
    private final BinDataImporter importer;

    public BinSeeder(BinDataImporter importer) {
        this.importer = importer;
    }

    @Bean
    @Order(4)
    @Profile({"default", "prod", "test"})
    public CommandLineRunner seedBinsOnStartup() {
        return args -> {
            logger.info(">>> Bin Seeding started");
            importer.importData();
            logger.info(">>> Bin Seeding completed");
        };
    }
}