package tech3.binitright.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tech3.binitright.JwtAuthFilter;
import tech3.binitright.interfacemethods.AdminInterface;
import tech3.binitright.interfacemethods.CheckInInterface;
import tech3.binitright.interfacemethods.IssueInterface;
import tech3.binitright.model.*;
import tech3.binitright.repository.ReportRepository;
import tech3.binitright.service.DigitalOceanStorageService;
import tech3.binitright.service.ForecastService;

@WithMockUser(
        username = "admin",
        roles = {"ADMIN"}
)
@WebMvcTest(
        controllers = AdminController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = true)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /* -------- Mock all dependencies -------- */

    @MockitoBean private AdminInterface adminService;
    @MockitoBean private CheckInInterface checkInService;
    @MockitoBean private IssueInterface issueService;
    @MockitoBean private ForecastService forecastService;
    @MockitoBean private DigitalOceanStorageService digitalOceanStorageService;
    @MockitoBean private ReportRepository reportRepository;

    /* --------------------------------
       DASHBOARD
       -------------------------------- */
    @Test
    void dashboard_success() throws Exception {

        when(issueService.getLatestIssuesForDashboard())
                .thenReturn(List.of(new Issue()));
        when(issueService.getTotalIssueCount())
                .thenReturn(5L);
        when(checkInService.getPendingCheckIns())
                .thenReturn(List.of(new CheckIn()));
        when(forecastService.getForecastData())
                .thenReturn(Map.of("2026", 12345));

        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("issues"))
                .andExpect(model().attributeExists("newCount"))
                .andExpect(model().attributeExists("pendingCheckIns"))
                .andExpect(model().attributeExists("forecastData"));
    }

    /* --------------------------------
       REVIEW CHECK-IN (GET)
       -------------------------------- */

    private CheckIn buildValidCheckIn() {
        User user = new User();
        user.setUsername("testuser");

        WasteCategories wc = new WasteCategories();
        wc.setName("Plastic");

        CheckIn checkIn = new CheckIn();
        checkIn.setStatus(CheckIn.Status.PROCESSING);
        checkIn.setFileName("video.mp4");
        checkIn.setUser(user);
        checkIn.setWasteCategories(wc);

        return checkIn;
    }

    @Test
    void review_checkin_success() throws Exception {

        when(adminService.reviewCheckIn(1L))
                .thenReturn(buildValidCheckIn());
        when(digitalOceanStorageService.generateSignedVideoUrl("video.mp4"))
                .thenReturn("signed-url");

        Principal principal = () -> "adminUser";

        mockMvc.perform(get("/admin/review/1").principal(principal))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("checkIn"))
                .andExpect(model().attribute("adminUsername", "adminUser"))
                .andExpect(model().attributeExists("signedVideoUrl"));
    }

    /* --------------------------------
       REVIEW DECISION (POST)
       -------------------------------- */
    @Test
    void review_decision_redirect() throws Exception {

        mockMvc.perform(post("/admin/review/1")
                        .param("status", "APPROVED")
                        .param("remarks", "Looks good"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/checkin"))
                .andExpect(flash().attributeExists("success"));
    }

    /* --------------------------------
       CHECK-IN LIST
       -------------------------------- */
    @Test
    void checkin_list_success() throws Exception {

        when(checkInService.getAllCheckIns())
                .thenReturn(List.of(new CheckIn()));

        mockMvc.perform(get("/admin/checkin"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("checkIns"));
    }

    /* --------------------------------
       SUSTAINABILITY REPORTS
       -------------------------------- */
    @Test
    void sustainability_reports_all() throws Exception {

        when(reportRepository.findAll())
                .thenReturn(List.of(new Report()));

        mockMvc.perform(get("/admin/sustainability-reports"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("allReports"));
    }

    /* --------------------------------
       FORECAST JSON ENDPOINT
       -------------------------------- */
    @Test
    void forecast_api_success() throws Exception {

        when(forecastService.getForecastData())
                .thenReturn(Map.of("2026", 9999));

        mockMvc.perform(get("/admin/forecast"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.2026").value(9999));
    }
}

