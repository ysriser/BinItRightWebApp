package techthree.binitright.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import techthree.binitright.service.BinDataImporter;

@RestController
@RequestMapping("/admin/import")
public class AdminImportController {

    private final BinDataImporter importer;

    public AdminImportController(BinDataImporter importer) {
        this.importer = importer;
    }

    private static final Logger log = LoggerFactory.getLogger(AdminImportController.class);

    @GetMapping("/bins")
    public String importBins() {
        log.info("Starting bin data import");
        importer.importData();
        log.info("Bin data import completed successfully");
        return "Bin data import completed successfully!";
    }
    
    
}