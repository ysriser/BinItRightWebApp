package tech3.binitright.service;

import org.springframework.stereotype.Service;
import tech3.binitright.repository.CheckInRepository;
import tech3.binitright.response.RecycleHistoryResponse;

import java.util.List;

@Service
public class RecycledHistoryService {

    private final CheckInRepository repository;

    public RecycledHistoryService(CheckInRepository repository) {
        this.repository = repository;
    }

    public List<RecycleHistoryResponse> getRecycleHistory(Long userId) {
        return repository.findRecycleHistoryByUserId(userId);
    }
}
