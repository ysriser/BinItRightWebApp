package tech3.binitright.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech3.binitright.interfacemethods.ReportInterface;
import tech3.binitright.repository.WasteCheckinRepository;

@Service
public final class ReportServiceImpl implements ReportInterface {

    @Autowired
    private WasteCheckinRepository checkinRepository;

    @Override
    public Map<String, Object> getSustainabilityStats(final int month, final int year) {
        final Map<String, Object> stats = new HashMap<>();

        // 1. Calculate weight for the selected month/year
        final Object rawTotalWeight = checkinRepository.calculateWeightByMonth(month, year);
        final Double totalWeight = (rawTotalWeight instanceof Number) ? ((Number) rawTotalWeight).doubleValue() : 0.0;

        // 2. Calculate CO2 for the selected month/year
        final Object rawCO2 = checkinRepository.calculateCO2ByMonth(month, year);
        final Double co2Saved = (rawCO2 instanceof Number) ? ((Number) rawCO2).doubleValue() : 0.0;

        // 3. Count unique users for the selected month/year
        final Long activeParticipants = checkinRepository.countParticipantsByMonth(month, year);

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