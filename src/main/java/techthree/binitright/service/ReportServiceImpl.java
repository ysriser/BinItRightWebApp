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

        //  Calculate weight for the selected month/year
        Object rawTotalWeight = checkinRepository.calculateWeightByMonth(month, year);

        Double totalWeight = (rawTotalWeight instanceof Number number)
                ? number.doubleValue()
                : 0.0;

        //  Calculate CO2 for the selected month/year
        Object rawCO2 = checkinRepository.calculateCO2ByMonth(month, year);

        Double co2Saved = (rawCO2 instanceof Number number)
                ? number.doubleValue()
                : 0.0;

        //  Count unique users for the selected month/year
        Long activeParticipants = checkinRepository.countParticipantsByMonth(month, year);

        List<Object[]> distribution = checkinRepository.getWeightDistributionByMonth(month, year); String mostRecycled = "N/A";
        double mostRecycledPercent = 0.0;
        if (!distribution.isEmpty() && totalWeight > 0) { Object[] top = distribution.get(0);
            mostRecycled = (String) top[0]; Double topWeight = ((Number) top[1]).doubleValue();
            mostRecycledPercent = (topWeight / totalWeight) * 100; }
        stats.put("totalWaste", totalWeight);
        stats.put("activeParticipants", activeParticipants != null ? activeParticipants : 0L);
        stats.put("co2Saved", co2Saved);
        stats.put("mostRecycled", mostRecycled);
        stats.put("mostRecycledPercent", Math.round(mostRecycledPercent));
        return stats; }
}

