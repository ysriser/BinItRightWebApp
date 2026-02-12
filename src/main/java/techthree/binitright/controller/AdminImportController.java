package techthree.binitright.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techthree.binitright.service.BinDataImporter;

@Controller
@RequestMapping("/admin/import")
public class AdminImportController {

    private final BinDataImporter importer;

    public AdminImportController(BinDataImporter importer) {
        this.importer = importer;
    }

    private static final Logger log = LoggerFactory.getLogger(AdminImportController.class);

    @PostMapping("/bins")
    public String importBins(RedirectAttributes redirectAttributes) {
        try {
            log.info("Starting bin data import");
            importer.importData();
            log.info("Bin data import completed successfully");

            redirectAttributes.addFlashAttribute("successMessage",
                    "Bin data import completed successfully!");

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("errorMessage",
                    "Bin import failed. Check logs.");
        }
        return "redirect:/admin/dashboard";
    }
    
    
}