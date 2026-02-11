package techthree.binitright.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techthree.binitright.interfacemethods.AdminInterface;
import techthree.binitright.interfacemethods.CheckInInterface;
import techthree.binitright.interfacemethods.IssueInterface;
import techthree.binitright.model.CheckIn;
import techthree.binitright.model.Report;
import techthree.binitright.repository.ReportRepository;
import techthree.binitright.service.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminInterface adminService;
    private final CheckInInterface checkInService;
    private final ForecastService forecastService;
    private final IssueInterface issueService;
    private final DigitalOceanStorageService digitalOceanStorageService;
    private final ReportRepository reportRepository;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private static final String CURRENT_PATH = "currentPath";

    public AdminController(AdminInterface adminService,
                           CheckInInterface checkInService,
                           ForecastService forecastService,
                           IssueInterface issueService,
                           DigitalOceanStorageService digitalOceanStorageService,
                           ReportRepository reportRepository) {
        this.adminService = adminService;
        this.checkInService = checkInService;
        this.forecastService = forecastService;
        this.issueService = issueService;
        this.digitalOceanStorageService = digitalOceanStorageService;
        this.reportRepository = reportRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute(CURRENT_PATH, "/admin/dashboard");
        model.addAttribute("issues",
                issueService.getLatestIssuesForDashboard());
        model.addAttribute("newCount",
                issueService.getTotalIssueCount());

        model.addAttribute(
                "pendingCheckIns",
                checkInService.getPendingCheckIns()
        );

        model.addAttribute(
                "forecastData",
                forecastService.getForecastData()
        );

        return "admin-dashboard";
    }

    @GetMapping("/review/{checkInId}")
    public String reviewCheckIn(@PathVariable Long checkInId, Model model, Principal principal) {
        CheckIn checkIn = adminService.reviewCheckIn(checkInId);
        String signedUrl = null;

        if (checkIn.getStatus() == CheckIn.Status.PROCESSING
                && checkIn.getFileName() != null) {
            signedUrl = digitalOceanStorageService
                    .generateSignedVideoUrl(checkIn.getFileName());
            model.addAttribute("signedVideoUrl", signedUrl);
        } else {
            logger.info("Video not added - Status: {}, FileName: {}",
                    checkIn.getStatus(), checkIn.getFileName());
        }

        model.addAttribute(CURRENT_PATH, "/admin/checkin");
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("adminUsername", principal.getName());

        return "checkin-review";
    }

    @PostMapping("/review/{id}")
    public String reviewDecision(
            @PathVariable("id") Long checkInId,
            @RequestParam("status") CheckIn.Status status,
            @RequestParam(required = false) String remarks,
            RedirectAttributes redirect) {

        adminService.updateCheckInStatus(checkInId, status, remarks);

        redirect.addFlashAttribute(
                "success",
                "Check-in " + status.name().toLowerCase() + " successfully"
        );

        return "redirect:/admin/checkin";
    }

    @GetMapping("/checkin")
    @Transactional(readOnly = true)
    public String checkinReviewList(Model model) {
        // Get all check-ins (you may want to filter by status)
        List<CheckIn> allCheckIns = checkInService.getAllCheckIns();
        model.addAttribute(CURRENT_PATH, "/admin/checkin");
        model.addAttribute("checkIns", allCheckIns);
        return "checkin-list";
    }
    @GetMapping("/sustainability-reports")
    public String showSustainabilityReports(
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "year", required = false) Integer year,
            Model model) {

        List<Report> reports;
        if (month != null && year != null) {
            reports = reportRepository.findByMonthAndYear(month, year);
        } else {
            reports = reportRepository.findAll();
        }
        model.addAttribute("allReports", reports);
        model.addAttribute(CURRENT_PATH, "/admin/sustainability-reports");

        return "sustainability-reports";
    }
    @GetMapping("/forecast")// using to check api end point in load test for python
    @ResponseBody
    public Map<String, Object> forecast() {
        return forecastService.getForecastData();
    }
}