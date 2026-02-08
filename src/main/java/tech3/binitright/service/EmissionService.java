package tech3.binitright.service;

import tech3.binitright.dto.EmissionCalculator;
import tech3.binitright.model.CheckIn;
import tech3.binitright.model.WasteCategories;
import tech3.binitright.repository.CheckInRepository;

import java.math.BigDecimal;
import java.util.List;

public class EmissionService {
    private final CheckInRepository checkInRepository;

    public EmissionService(CheckInRepository checkInRepository) {
        this.checkInRepository = checkInRepository;
    }

    public BigDecimal getUserTotalCo2Saved(Long userId) {

        List<CheckIn> approved =
                checkInRepository.findApprovedByUserIdWithCategory(userId);

        BigDecimal total = BigDecimal.ZERO;

        for (CheckIn ci : approved) {
            WasteCategories wc = ci.getWasteCategories();
            if (wc == null) continue;

            BigDecimal co2 = EmissionCalculator.co2SavedKg(
                    ci.getQuantity(),
                    wc.getAvgWeight(),
                    wc.getEmissionFactor()
            );

            total = total.add(co2);
        }
        return total;
    }


    public Long getPendingApprovals(Long userId) {
        return checkInRepository.countPendingByUserId(userId);
    }
}

