package tech3.binitright.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import tech3.binitright.interfacemethods.IssueInterface;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.Issue;
import tech3.binitright.model.Issue.IssueCategory;
import tech3.binitright.request.IssueCreateRequest;
import tech3.binitright.response.IssueResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class IssueRestControllerTest {

    private IssueRestController controller;
    private IssueInterface issueService;
    private UserInterface userService;

    @BeforeEach
    void setUp() {
        issueService = Mockito.mock(IssueInterface.class);
        controller = new IssueRestController(issueService);
    }

    @Test
    void createIssue_ValidRequest_ReturnsSavedIssueId() {

        Authentication auth = Mockito.mock(Authentication.class);
        when(auth.getName()).thenReturn("101");


        IssueCreateRequest request = new IssueCreateRequest(
                "BinIssues",
                "The bin at Block 22 is overflowing.",
                101L
        );

        Issue savedIssue = new Issue();
        savedIssue.setIssueId(500L);


        when(issueService.createIssue(eq(request), eq(101L))).thenReturn(savedIssue);

        // Act
        ResponseEntity<IssueResponse> response = controller.createIssue(request, auth);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500L, response.getBody().getIssueId());
    }
}