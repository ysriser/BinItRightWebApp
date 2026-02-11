package techthree.binitright.dto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
public class EmissionCalculatorTest {
    private static void assertBigDecimalEquals(String expected, BigDecimal actual) {
        assertNotNull(actual);
        assertEquals(0, actual.compareTo(new BigDecimal(expected)),
                "Expected " + expected + " but got " + actual);
    }

    @Test
    void recycledKg_whenQuantityIsNull_returnsZero() {
        BigDecimal result = EmissionCalculator.recycledKg(null, new BigDecimal("0.5"));
        assertBigDecimalEquals("0", result);
    }

    @Test
    void recycledKg_whenQuantityIsZero_returnsZero() {
        BigDecimal result = EmissionCalculator.recycledKg(0, new BigDecimal("0.5"));
        assertBigDecimalEquals("0", result);
    }

    @Test
    void recycledKg_whenAvgWeightIsNull_returnsZero() {
        BigDecimal result = EmissionCalculator.recycledKg(5, null);
        assertBigDecimalEquals("0", result);
    }

    @Test
    void recycledKg_validInputs_returnsCorrectKg() {
        BigDecimal result = EmissionCalculator.recycledKg(4, new BigDecimal("0.25"));
        assertBigDecimalEquals("1.0", result); // 4 * 0.25 = 1
    }

    @Test
    void co2SavedKg_whenEmissionFactorIsNull_returnsZero() {
        BigDecimal result = EmissionCalculator.co2SavedKg(5, new BigDecimal("1.0"), null);
        assertBigDecimalEquals("0", result);
    }

    @Test
    void co2SavedKg_whenAllInputsValid_returnsCorrectValue() {
        BigDecimal result = EmissionCalculator.co2SavedKg(
                10,
                new BigDecimal("0.5"),
                new BigDecimal("2.0")
        );
        assertBigDecimalEquals("10", result); // 10 * 0.5 = 5; 5 * 2 = 10
    }

    @Test
    void co2SavedKg_whenQuantityIsZero_returnsZero() {
        BigDecimal result = EmissionCalculator.co2SavedKg(
                0,
                new BigDecimal("0.5"),
                new BigDecimal("2.0")
        );
        assertBigDecimalEquals("0", result);
    }
}
