package bg.sofia.uni.fmi.mjt.myfitnesspal.diary;

import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FoodEntryTest {

    @Test
    void testFoodEntryFoodIsNull() {
        NutritionInfo n = new NutritionInfo(30, 50, 20);
        assertThrows(IllegalArgumentException.class, () -> new FoodEntry(null, 40, n), "Food cannot be null");
    }

    @Test
    void testFoodEntryFoodIsBlank() {
        NutritionInfo n = new NutritionInfo(30, 50, 20);
        assertThrows(IllegalArgumentException.class, () -> new FoodEntry("", 40, n), "Food cannot be blank");
    }

    @Test
    void testNutritionInfoIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new FoodEntry("food", 40, null), "NutritionInfo cannot be null");
    }

    @Test
    void testFoodEntryServingSizeIsNegative() {
        NutritionInfo n = new NutritionInfo(30, 50, 20);
        assertThrows(IllegalArgumentException.class, () -> new FoodEntry("food", -5, n), "Food cannot be blank");
    }

}
