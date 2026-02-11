package techthree.binitright.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import techthree.binitright.service.BinDataImporter;

@Configuration
public class BinSeeder {

    private static final Logger log = LoggerFactory.getLogger(BinSeeder.class);

    private final BinDataImporter importer;

    public BinSeeder(BinDataImporter importer) {
        this.importer = importer;
    }

    @Bean
    @Order(4)
    @Profile({"default", "prod", "test"})
    public CommandLineRunner seedBinsOnStartup() {
        return args -> {
            log.info(">>> Bin Seeding started");
            importer.importData();
            log.info(">>> Bin Seeding completed");
        };
    }
}