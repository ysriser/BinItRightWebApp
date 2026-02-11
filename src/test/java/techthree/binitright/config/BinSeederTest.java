package techthree.binitright.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.CommandLineRunner;
import techthree.binitright.service.BinDataImporter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class BinSeederTest {

    private BinSeeder binSeeder;
    private BinDataImporter binDataImporter;

    @BeforeEach
    void setUp() {
        binDataImporter = Mockito.mock(BinDataImporter.class);
        binSeeder = new BinSeeder(binDataImporter);
    }

    @Test
    void seedBinsOnStartup_ShouldExecuteImporter() throws Exception {

        CommandLineRunner runner = binSeeder.seedBinsOnStartup();
        assertNotNull(runner);
        runner.run();
        verify(binDataImporter, times(1)).importData();
    }
}
