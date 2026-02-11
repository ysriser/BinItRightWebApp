package techthree.binitright.service;

import org.springframework.stereotype.Service;
import techthree.binitright.dto.EmissionCalculator;
import techthree.binitright.model.CheckIn;
import techthree.binitright.model.WasteCategories;
import techthree.binitright.repository.CheckInRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
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

