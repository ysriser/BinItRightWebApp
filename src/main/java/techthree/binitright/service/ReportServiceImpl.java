package techthree.binitright.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techthree.binitright.interfacemethods.ReportInterface;
import techthree.binitright.repository.WasteCheckinRepository;

import java.util.*;

@Service
public class ReportServiceImpl implements ReportInterface {

    @Autowired
    private WasteCheckinRepository checkinRepository;

    @Override
    public Map<String, Object> getSustainabilityStats(int month, int year) {
        Map<String, Object> stats = new HashMap<>();

        // 1. Calculate weight for the selected month/year
        Object rawTotalWeight = checkinRepository.calculateWeightByMonth(month, year);
        Double totalWeight = (rawTotalWeight instanceof Number) ? ((Number) rawTotalWeight).doubleValue() : 0.0;

        // 2. Calculate CO2 for the selected month/year
        Object rawCO2 = checkinRepository.calculateCO2ByMonth(month, year);
        Double co2Saved = (rawCO2 instanceof Number) ? ((Number) rawCO2).doubleValue() : 0.0;

        // 3. Count unique users for the selected month/year
        Long activeParticipants = checkinRepository.countParticipantsByMonth(month, year);

        stats.put("totalWaste", totalWeight);
        stats.put("activeParticipants", activeParticipants != null ? activeParticipants : 0L);
        stats.put("co2Saved", co2Saved);

        // Optional: If you need distribution for the most recycled material
        // You would need to add a monthly version of getWeightDistributionByCategory to the repo
        stats.put("mostRecycled", "N/A");
        stats.put("mostRecycledPercent", 0);
        stats.put("recyclingRate", 78);

        return stats;
    }
}