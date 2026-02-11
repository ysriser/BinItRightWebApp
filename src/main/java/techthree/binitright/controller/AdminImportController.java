package techthree.binitright.controller;

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

    @GetMapping("/bins")
    public String importBins() {
    	System.out.println("=== Starting import ===");
        importer.importData();
        System.out.println("=== Import completed ===");
        return "Bin data import completed successfully!";
    }
    
    
}