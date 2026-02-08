package tech3.binitright.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech3.binitright.response.RecycleHistoryResponse;
import tech3.binitright.service.RecycleHistoryService;

@RestController
@RequestMapping("/api")
public class RecycleHistoryController {

    private final RecycleHistoryService recycleHistoryService;

    public RecycleHistoryController(final RecycleHistoryService recycleHistoryService) {
        this.recycleHistoryService = recycleHistoryService;
    }
    @GetMapping("/recycle-history")
    public List<RecycleHistoryResponse> getRecycleHistory(final Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        final String userIdStr = (String) authentication.getPrincipal();
        final Long userId = Long.valueOf(userIdStr);

        return recycleHistoryService.getRecycleHistory(userId);
    }
}