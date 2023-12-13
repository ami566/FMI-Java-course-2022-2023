package bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NutritionInfoTest {

    @Test
    void testCarbohydratesIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(-15, 50, 60),
                "Carbohydrates cannot be negative");
    }

    @Test
    void testFatsIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(15, -50, 60),
                "Fats cannot be negative");
    }

    @Test
    void testProteinsIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(15, 50, -60),
                "Proteins cannot be negative");
    }

    @Test
    void testNutrientsSumNotEqualTo100() {
        assertThrows(IllegalArgumentException.class, () -> new NutritionInfo(15, 50, 34),
                "The sum of all nutrients does not equal 100");
    }

    @Test
    void testCaloriesCalculationCorrectSum() {
        double calories = 25 * MacroNutrient.PROTEIN.calories +
                60 * MacroNutrient.FAT.calories +
                15 * MacroNutrient.CARBOHYDRATE.calories;
        NutritionInfo n = new NutritionInfo(25, 60, 15);
        assertEquals(calories, n.calories(), "Calories are not calculated right");
    }

    @Test
    void testNutritionInfoWithCorrectArguments() {
        NutritionInfo n = new NutritionInfo(10, 20, 70);
        assertNotEquals(n, new NutritionInfo(20, 30, 50),
                "Constructing different NutritionInfo should return different objects");
    }
}
