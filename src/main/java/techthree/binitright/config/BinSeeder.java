package techthree.binitright.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import techthree.binitright.service.BinDataImporter;

@Configuration
public class BinSeeder {

    private final BinDataImporter importer;

    public BinSeeder(BinDataImporter importer) {
        this.importer = importer;
    }

    @Bean
    @Order(4)
    @Profile({"default", "prod", "test"})
    public CommandLineRunner seedBinsOnStartup() {
        return args -> {
            System.out.println(">>> Bin Seeding started");
            importer.importData();
            System.out.println(">>> Bin Seeding completed");
        };
    }
}