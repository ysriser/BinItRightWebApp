package tech3.binitright.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech3.binitright.interfacemethods.IssueInterface;
import tech3.binitright.interfacemethods.UserInterface;
import tech3.binitright.model.Issue;
import tech3.binitright.request.IssueCreateRequest;
import tech3.binitright.response.IssueResponse;
import tech3.binitright.service.UserImplementation;

@RestController
@RequestMapping("/api/issues")
public class IssueRestController {

    @Autowired
    private UserInterface userService;

    @Autowired
    public void setUserService(final UserImplementation userImplementation) {
        this.userService = userImplementation;
    }

    private final IssueInterface issueService;

    public IssueRestController(final IssueInterface issueService) {
        this.issueService = issueService;
    }

    @PostMapping
    public ResponseEntity<IssueResponse> createIssue(
            @RequestBody final IssueCreateRequest request, final Authentication authentication) {

        final Long userId = Long.valueOf(authentication.getName());
        final Issue saved = issueService.createIssue(request, userId);

        final IssueResponse response = new IssueResponse(saved.getIssueId());
        return ResponseEntity.ok(response);
    }
}

