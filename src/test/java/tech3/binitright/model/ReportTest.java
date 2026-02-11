package tech3.binitright.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ReportTest {
    @Test
    void settersAndGetters_shouldWork() {
        Report report = new Report();

        Admin admin = new Admin();
        LocalDateTime t = LocalDateTime.of(2026, 2, 11, 15, 0);

        report.setReportId(1L);
        report.setAdmin(admin);
        report.setGeneratedAt(t);

        assertEquals(1L, report.getReportId());
        assertSame(admin, report.getAdmin());
        assertEquals(t, report.getGeneratedAt());
    }

    @Test
    void allArgsConstructor_shouldSetFields() {
        Admin admin = new Admin();
        LocalDateTime t = LocalDateTime.of(2026, 2, 10, 9, 30);

        Report report = new Report(10L, admin, t);

        assertEquals(10L, report.getReportId());
        assertSame(admin, report.getAdmin());
        assertEquals(t, report.getGeneratedAt());
    }
}
