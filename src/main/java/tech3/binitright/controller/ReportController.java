package tech3.binitright.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import tech3.binitright.interfacemethods.ReportInterface;
import tech3.binitright.model.Admin;
import tech3.binitright.model.Report;
import tech3.binitright.repository.AdminRepository;
import tech3.binitright.repository.ReportRepository;



@Controller
@RequestMapping("/admin/report")
public class ReportController {

    @Autowired private ReportInterface reportService;
    @Autowired private ReportRepository reportRepository;
    @Autowired private AdminRepository adminRepository;


    @PostMapping("/generate")
    public String generateNewReport(@RequestParam("month") final int month,
                                    @RequestParam("year") final int year) {
        final Report log = new Report();
        log.setGeneratedAt(LocalDateTime.now());
        final Admin admin = adminRepository.findById(1L).orElse(null);
        log.setAdmin(admin);
        reportRepository.save(log);
        return "redirect:/admin/sustainability-reports";
    }


    @GetMapping("/view/{id}")
    public String viewSpecificReport(@PathVariable("id") final Long id, final Model model) {
        final Report reportLog = reportRepository.findById(id).orElseThrow();
        final int month = reportLog.getGeneratedAt().getMonthValue();
        final int year = reportLog.getGeneratedAt().getYear();
        return populateReportView(month, year, reportLog.getReportId(), model);
    }
    private String populateReportView(final int month, final int year, final Long reportId, final Model model) {
        final Map<String, Object> stats = reportService.getSustainabilityStats(month, year);
        final String monthName = Month.of(month).name();

        model.addAttribute("stats", stats);
        model.addAttribute("reportMonth", monthName.charAt(0) + monthName.substring(1).toLowerCase());
        model.addAttribute("reportYear", year);
        model.addAttribute("reportId", "RPT-" + reportId);

        return "view-report";
    }
    @GetMapping("/download/csv/{id}")
    public void downloadReportCsv(
            @PathVariable final Long id,
            final HttpServletResponse response) throws IOException {

        final Report report = reportRepository.findById(id).orElseThrow();

        final int month = report.getGeneratedAt().getMonthValue();
        final int year = report.getGeneratedAt().getYear();

        final Map<String, Object> stats =
                reportService.getSustainabilityStats(month, year);

        response.setContentType("text/csv");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=RPT-" + id + ".csv"
        );

        final PrintWriter writer = response.getWriter();

        // CSV Header
        writer.println("Metric,Value");

        writer.println("Report ID,RPT-" + id);
        writer.println("Month," + month);
        writer.println("Year," + year);
        writer.println("Total Waste Collected (kg)," + stats.get("totalWaste"));
        writer.println("Recycling Rate (%)," + stats.get("mostRecycledPercent"));
        writer.println("Active Participants," + stats.get("activeParticipants"));
        writer.println("CO2 Emissions Avoided (tons)," + stats.get("co2Saved"));
        writer.println("Most Recycled Material," + stats.get("mostRecycled"));

        writer.flush();
    }
    @GetMapping("/download/zip")
    public void downloadReportsZip(
            @RequestParam("ids") final List<Long> ids,
            final HttpServletResponse response) throws IOException {

        response.setContentType("application/zip");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=sustainability-reports.zip"
        );

        final ZipOutputStream zipOut =
                new ZipOutputStream(response.getOutputStream());

        for (final Long id : ids) {
            final Report report = reportRepository.findById(id).orElse(null);
            if (report == null) {
				continue;
			}

            final int month = report.getGeneratedAt().getMonthValue();
            final int year = report.getGeneratedAt().getYear();

            final Map<String, Object> stats =
                    reportService.getSustainabilityStats(month, year);

            final String fileName = "RPT-" + id + ".csv";

            zipOut.putNextEntry(new ZipEntry(fileName));

            final StringBuilder csv = new StringBuilder();
            csv.append("Metric,Value\n");
            csv.append("Report ID,RPT-").append(id).append("\n");
            csv.append("Month,").append(month).append("\n");
            csv.append("Year,").append(year).append("\n");
            csv.append("Total Waste Collected (kg),").append(stats.get("totalWaste")).append("\n");
            csv.append("Recycling Rate (%),").append(stats.get("mostRecycledPercent")).append("\n");
            csv.append("Active Participants,").append(stats.get("activeParticipants")).append("\n");
            csv.append("CO2 Emissions Avoided (tons),").append(stats.get("co2Saved")).append("\n");
            csv.append("Most Recycled Material,").append(stats.get("mostRecycled")).append("\n");

            zipOut.write(csv.toString().getBytes(StandardCharsets.UTF_8));
            zipOut.closeEntry();
        }

        zipOut.finish();
        zipOut.close();
    }


}