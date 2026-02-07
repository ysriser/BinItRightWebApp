package tech3.binitright.service;

import org.springframework.stereotype.Service;
import tech3.binitright.repository.CheckInRepository;
import tech3.binitright.response.RecycleHistoryResponse;

import java.util.List;

@Service
public class RecycleHistoryService {

    private final CheckInRepository repository;

    public RecycleHistoryService(CheckInRepository repository) {
        this.repository = repository;
    }

    public List<RecycleHistoryResponse> getRecycleHistory(Long userId) {
        List<RecycleHistoryResponse> history =repository.findRecycleHistoryByUserId(userId);
        if (history == null || history.isEmpty()) {
            return List.of();
        }
        return history;
    }
}
