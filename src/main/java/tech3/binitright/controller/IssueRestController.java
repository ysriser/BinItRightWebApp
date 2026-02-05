package tech3.binitright.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech3.binitright.interfacemethods.IssueInterface;
import tech3.binitright.request.IssueCreateRequest;
import tech3.binitright.model.Issue;
import tech3.binitright.response.IssueResponse;

@RestController
@RequestMapping("/api/issues")
public class IssueRestController {

    private final IssueInterface issueService;

    public IssueRestController(IssueInterface issueService) {
        this.issueService = issueService;
    }

    @PostMapping
    public ResponseEntity<IssueResponse> createIssue(
            @RequestBody IssueCreateRequest request) {

        Issue saved = issueService.createIssue(request);

        IssueResponse response = new IssueResponse(saved.getIssueId());
        return ResponseEntity.ok(response);
    }
}

