package tech3.binitright.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tech3.binitright.interfacemethods.AdminInterface;
import tech3.binitright.interfacemethods.CheckInInterface;
import tech3.binitright.interfacemethods.IssueInterface;
import tech3.binitright.model.CheckIn;
import tech3.binitright.model.Issue;
import tech3.binitright.model.Report;
import tech3.binitright.repository.ReportRepository;
import tech3.binitright.service.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminInterface adminService;

    public void setAdminService(AdminImplementation adminserviceImp) {
        this.adminService = adminserviceImp;
    }

    @Autowired
    private CheckInInterface checkInService;

    public void setcheckInService(CheckInImplementation checkInserviceImp) {
        this.checkInService = checkInserviceImp;
    }

    @Autowired
    private ForecastService forecastService;

    public void setForecastService(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    @Autowired
    private IssueInterface issueService;

    public void setIssueService(IssueImplementation issueserviceImp) {
        this.issueService = issueserviceImp;
    }

    @Autowired
    private DigitalOceanStorageService digitalOceanStorageService;
    @Autowired private ReportRepository reportRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        List<Issue> issues = issueService.getAllIssues();

        model.addAttribute("currentPath", "/admin/dashboard");
        model.addAttribute("issues",
                issueService.getLatestIssuesForDashboard());
        model.addAttribute("newCount",
                issueService.getTotalIssueCount());

        model.addAttribute(
                "pendingCheckIns",
                checkInService.getPendingCheckIns()
        );

//                  model.addAttribute(
//                       "forecastData",
//                       forecastService.getForecastData()
//                 );

        return "admin-dashboard";
    }

    @GetMapping("/review/{checkInId}")
    public String reviewCheckIn(@PathVariable Long checkInId, Model model, Principal principal) {
        CheckIn checkIn = adminService.reviewCheckIn(checkInId);
        String signedUrl = null;;

        if (checkIn.getStatus() == CheckIn.Status.PROCESSING
                && checkIn.getFileName() != null) {
            signedUrl = digitalOceanStorageService
                    .generateSignedVideoUrl(checkIn.getFileName());
            model.addAttribute("signedVideoUrl", signedUrl);
        } else {
            System.out.println("Video not added - Status: " + checkIn.getStatus()
                    + ", FileName: " + checkIn.getFileName());
        }

        model.addAttribute("currentPath", "/admin/checkin");
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

        return "redirect:/admin/dashboard";
    }

    @GetMapping("/checkin")
    @Transactional(readOnly = true)
    public String checkinReviewList(Model model) {
        // Get all check-ins (you may want to filter by status)
        List<CheckIn> allCheckIns = checkInService.getAllCheckIns();
        model.addAttribute("currentPath", "/admin/checkin");
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
        model.addAttribute("currentPath", "/admin/sustainability-reports");

       return "sustainability-reports";
    }
}
