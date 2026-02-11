package techthree.binitright.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import techthree.binitright.JwtAuthFilter;
import techthree.binitright.interfacemethods.ReportInterface;
import techthree.binitright.model.Admin;
import techthree.binitright.model.Report;
import techthree.binitright.repository.AdminRepository;
import techthree.binitright.repository.ReportRepository;

@WebMvcTest(
        controllers = ReportController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthFilter.class
        )
)
@WithMockUser(roles = "ADMIN")
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private ReportInterface reportService;
    @MockitoBean private ReportRepository reportRepository;
    @MockitoBean private AdminRepository adminRepository;

    private Report mockReport;

    @BeforeEach
    void setUp() {
        mockReport = new Report();
        mockReport.setReportId(101L);
        mockReport.setGeneratedAt(LocalDateTime.of(2026, 2, 10, 10, 0));
    }

    @Test
    void generateNewReport_ShouldRedirect() throws Exception {
        when(adminRepository.findById(1L)).thenReturn(Optional.of(new Admin()));

        mockMvc.perform(post("/admin/report/generate")
                        .param("month", "2")
                        .param("year", "2026")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/sustainability-reports"));

        verify(reportRepository, times(1)).save(any(Report.class));
    }

    @Test
    void downloadReportCsv_ShouldReturnCsvFile() throws Exception {
        when(reportRepository.findById(101L)).thenReturn(Optional.of(mockReport));
        when(reportService.getSustainabilityStats(2, 2026))
                .thenReturn(Map.of("totalWaste", 100, "co2Saved", 50));

        mockMvc.perform(get("/admin/report/download/csv/101"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/csv"))
                .andExpect(header().string("Content-Disposition", "attachment; filename=RPT-101.csv"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Metric,Value")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Total Waste Collected (kg),100")));
    }

    @Test
    void downloadReportsZip_ShouldReturnZipFile() throws Exception {
        when(reportRepository.findById(101L)).thenReturn(Optional.of(mockReport));
        when(reportService.getSustainabilityStats(2, 2026))
                .thenReturn(Map.of("totalWaste", 100));

        mockMvc.perform(get("/admin/report/download/zip")
                        .param("ids", "101"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/zip"))
                .andExpect(header().string("Content-Disposition", "attachment; filename=sustainability-reports.zip"));
    }
}
