package tech3.binitright.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech3.binitright.interfacemethods.IssueInterface;
import tech3.binitright.request.IssueCreateRequest;
import tech3.binitright.model.Issue;

@RestController
@RequestMapping("/api/issues")
public class IssueRestController {

    private final IssueInterface issueService;

    public IssueRestController(IssueInterface issueService) {
        this.issueService = issueService;
    }

    @PostMapping
    public ResponseEntity<Long> createIssue(
            @RequestBody IssueCreateRequest request) {

        Issue saved = issueService.createIssue(request);
        return ResponseEntity.ok(saved.getIssueId());
    }
}

