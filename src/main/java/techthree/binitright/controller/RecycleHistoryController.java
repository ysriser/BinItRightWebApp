package techthree.binitright.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import techthree.binitright.response.RecycleHistoryResponse;
import techthree.binitright.service.RecycleHistoryService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RecycleHistoryController {

    private final RecycleHistoryService recycleHistoryService;

    public RecycleHistoryController(RecycleHistoryService recycleHistoryService) {
        this.recycleHistoryService = recycleHistoryService;
    }
    @GetMapping("/recycle-history")
    public List<RecycleHistoryResponse> getRecycleHistory(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new org.springframework.security.access.AccessDeniedException("Unauthorized");
        }

        String userIdStr = (String) authentication.getPrincipal();
        Long userId = Long.valueOf(userIdStr);

        return recycleHistoryService.getRecycleHistory(userId);
    }
}