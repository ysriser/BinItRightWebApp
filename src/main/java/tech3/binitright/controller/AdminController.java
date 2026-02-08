package tech3.binitright.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tech3.binitright.interfacemethods.AdminInterface;
import tech3.binitright.interfacemethods.CheckInInterface;
import tech3.binitright.interfacemethods.IssueInterface;
import tech3.binitright.model.CheckIn;
import tech3.binitright.model.Report;
import tech3.binitright.repository.ReportRepository;
import tech3.binitright.service.AdminImplementation;
import tech3.binitright.service.CheckInImplementation;
import tech3.binitright.service.DigitalOceanStorageService;
import tech3.binitright.service.ForecastService;
import tech3.binitright.service.IssueImplementation;

@Controller
@RequestMapping("/admin")
public final class AdminController {

    @Autowired
    private AdminInterface adminService;

    public void setAdminService(final AdminImplementation adminserviceImp) {
        this.adminService = adminserviceImp;
    }

    @Autowired
    private CheckInInterface checkInService;

    public void setcheckInService(final CheckInImplementation checkInserviceImp) {
        this.checkInService = checkInserviceImp;
    }

    @Autowired
    private ForecastService forecastService;

    public void setForecastService(final ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    @Autowired
    private IssueInterface issueService;

    public void setIssueService(final IssueImplementation issueserviceImp) {
        this.issueService = issueserviceImp;
    }

    @Autowired
    private DigitalOceanStorageService digitalOceanStorageService;
    
    @Autowired 
    private ReportRepository reportRepository;

    @GetMapping("/dashboard")
    public String dashboard(final Model model) {
        model.addAttribute("currentPath", "/admin/dashboard");
        model.addAttribute("issues", issueService.getLatestIssuesForDashboard());
        model.addAttribute("newCount", issueService.getTotalIssueCount());
        model.addAttribute("pendingCheckIns", checkInService.getPendingCheckIns());
        model.addAttribute("forecastData", forecastService.getForecastData());
        return "admin-dashboard";
    }

    @GetMapping("/review/{checkInId}")
    public String reviewCheckIn(@PathVariable final Long checkInId, final Model model, final Principal principal) {
        final CheckIn checkIn = adminService.reviewCheckIn(checkInId);
        String signedUrl = null;

        if (checkIn.getStatus() == CheckIn.Status.PROCESSING && checkIn.getFileName() != null) {
            signedUrl = digitalOceanStorageService.generateSignedVideoUrl(checkIn.getFileName());
            model.addAttribute("signedVideoUrl", signedUrl);
        }

        model.addAttribute("currentPath", "/admin/checkin");
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("adminUsername", principal.getName());
        return "checkin-review";
    }

    @PostMapping("/review/{id}")
    public String reviewDecision(@PathVariable("id") final Long checkInId,
                                 @RequestParam("status") final CheckIn.Status status,
                                 @RequestParam(required = false) final String remarks,
                                 final RedirectAttributes redirect) {
        adminService.updateCheckInStatus(checkInId, status, remarks);
        redirect.addFlashAttribute("success", "Check-in " + status.name().toLowerCase() + " successfully");
        return "redirect:/admin/checkin";
    }

    @GetMapping("/checkin")
    @Transactional(readOnly = true)
    public String checkinReviewList(final Model model) {
        final List<CheckIn> allCheckIns = checkInService.getAllCheckIns();
        model.addAttribute("currentPath", "/admin/checkin");
        model.addAttribute("checkIns", allCheckIns);
        return "checkin-list";
    }

    @GetMapping("/sustainability-reports")
    public String showSustainabilityReports(@RequestParam(value = "month", required = false) final Integer month,
                                            @RequestParam(value = "year", required = false) final Integer year,
                                            final Model model) {
        final List<Report> reports;
        if (month != null && year != null) {
            reports = reportRepository.findByMonthAndYear(month, year);
        } else {
            reports = reportRepository.findAll();
        }
        model.addAttribute("allReports", reports);
        model.addAttribute("currentPath", "/admin/sustainability-reports");
        return "sustainability-reports";
    }

    @GetMapping("/forecast")
    @ResponseBody
    public Map<String, Object> forecast() {
        return forecastService.getForecastData();
    }
}