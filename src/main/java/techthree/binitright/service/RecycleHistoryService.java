package techthree.binitright.service;

import org.springframework.stereotype.Service;
import techthree.binitright.repository.CheckInRepository;
import techthree.binitright.response.RecycleHistoryResponse;

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
