package tech3.binitright.service;

import java.util.List;

import org.springframework.stereotype.Service;

import tech3.binitright.repository.CheckInRepository;
import tech3.binitright.response.RecycleHistoryResponse;

@Service
public class RecycleHistoryService {

    private final CheckInRepository repository;

    public RecycleHistoryService(final CheckInRepository repository) {
        this.repository = repository;
    }

    public List<RecycleHistoryResponse> getRecycleHistory(final Long userId) {
        final List<RecycleHistoryResponse> history =repository.findRecycleHistoryByUserId(userId);
        if (history == null || history.isEmpty()) {
            return List.of();
        }
        return history;
    }
}
