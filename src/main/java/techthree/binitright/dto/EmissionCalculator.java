package techthree.binitright.dto;

import java.math.BigDecimal;

public class EmissionCalculator {

    private EmissionCalculator() {}

    private static BigDecimal safe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    public static BigDecimal recycledKg(Integer qty, BigDecimal avgWeightKgPerItem) {
        if (qty == null || qty <= 0) return BigDecimal.ZERO;
        return safe(avgWeightKgPerItem).multiply(BigDecimal.valueOf(qty));
    }

    public static BigDecimal co2SavedKg(Integer qty, BigDecimal avgWeightKgPerItem, BigDecimal emissionFactorKgPerKg) {
        BigDecimal kg = recycledKg(qty, avgWeightKgPerItem);
        return kg.multiply(safe(emissionFactorKgPerKg));
    }
}

