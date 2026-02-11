package techthree.binitright.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import techthree.binitright.interfacemethods.IssueInterface;
import techthree.binitright.request.IssueCreateRequest;
import techthree.binitright.model.Issue;
import techthree.binitright.response.IssueResponse;

@RestController
@RequestMapping("/api/issues")
public class IssueRestController {

    private final IssueInterface issueService;

    public IssueRestController(IssueInterface issueService) {
        this.issueService = issueService;
    }

    @PostMapping
    public ResponseEntity<IssueResponse> createIssue(
            @RequestBody IssueCreateRequest request, Authentication authentication) {

        Long userId = Long.valueOf(authentication.getName());
        Issue saved = issueService.createIssue(request, userId);

        IssueResponse response = new IssueResponse(saved.getIssueId());
        return ResponseEntity.ok(response);
    }
}

