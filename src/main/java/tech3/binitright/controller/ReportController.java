package tech3.binitright.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tech3.binitright.interfacemethods.ReportInterface;
import tech3.binitright.model.Admin;
import tech3.binitright.model.Report;
import tech3.binitright.repository.AdminRepository;
import tech3.binitright.repository.ReportRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Map;

@Controller
@RequestMapping("/admin/report")
public class ReportController {

    @Autowired private ReportInterface reportService;
    @Autowired private ReportRepository reportRepository;
    @Autowired private AdminRepository adminRepository;


    @GetMapping("/sustainability-reports")
    public String filterReportArchives(
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "year", required = false) Integer year,
            Model model) {
        model.addAttribute("allReports", reportRepository.findAll());
        model.addAttribute("currentPath", "/admin/sustainability-reports");
        return "sustainability-reports";
    }

    @PostMapping("/generate")
    public String generateNewReport(@RequestParam("month") int month,
                                    @RequestParam("year") int year) {
        Report log = new Report();
        log.setGeneratedAt(LocalDateTime.now());
        Admin admin = adminRepository.findById(1L).orElse(null);
        log.setAdmin(admin);
        reportRepository.save(log);
        return "redirect:/admin/report/sustainability-reports";
    }


    @GetMapping("/view/{id}")
    public String viewSpecificReport(@PathVariable("id") Long id, Model model) {
        Report reportLog = reportRepository.findById(id).orElseThrow();
        int month = reportLog.getGeneratedAt().getMonthValue();
        int year = reportLog.getGeneratedAt().getYear();
        return populateReportView(month, year, reportLog.getReportId(), model);
    }
    private String populateReportView(int month, int year, Long reportId, Model model) {
        Map<String, Object> stats = reportService.getSustainabilityStats(month, year);
        String monthName = Month.of(month).name();

        model.addAttribute("stats", stats);
        model.addAttribute("reportMonth", monthName.charAt(0) + monthName.substring(1).toLowerCase());
        model.addAttribute("reportYear", year);
        model.addAttribute("reportId", "RPT-" + reportId);

        return "view-report";
    }
}