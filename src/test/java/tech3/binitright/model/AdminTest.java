package tech3.binitright.model;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class AdminTest {
    @Test
    void defaultConstructor_and_setters_shouldWork() {
        Admin admin = new Admin();

        admin.setPermissionLevel("HIGH");
        admin.setDepartment("IT");

        List<Issue> issues = new ArrayList<>();
        admin.setIssue(issues);

        assertEquals("HIGH", admin.getPermissionLevel());
        assertEquals("IT", admin.getDepartment());
        assertSame(issues, admin.getIssue());
    }

    @Test
    void allArgsConstructor_shouldSetFields() {
        List<Issue> issues = new ArrayList<>();
        Admin admin = new Admin("MEDIUM", "Support", issues);

        assertEquals("MEDIUM", admin.getPermissionLevel());
        assertEquals("Support", admin.getDepartment());
        assertSame(issues, admin.getIssue());
    }

    @Test
    void setters_shouldOverrideExistingValues() {
        Admin admin = new Admin();

        admin.setPermissionLevel("LOW");
        admin.setDepartment("Ops");

        admin.setPermissionLevel("HIGH");
        admin.setDepartment("IT");

        assertEquals("HIGH", admin.getPermissionLevel());
        assertEquals("IT", admin.getDepartment());
    }
}
