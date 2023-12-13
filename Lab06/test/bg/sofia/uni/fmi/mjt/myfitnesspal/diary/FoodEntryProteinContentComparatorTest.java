package bg.sofia.uni.fmi.mjt.myfitnesspal.diary;

import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FoodEntryProteinContentComparatorTest {

    @Test
    void testCompareSecondProteinContentIsGreaterThanFirst() {
        NutritionInfo n1 = new NutritionInfo(30, 60, 10);
        NutritionInfo n2 = new NutritionInfo(50, 30, 20);

        FoodEntry f1 = new FoodEntry("food1", 400, n1);
        FoodEntry f2 = new FoodEntry("food2", 400, n2);

        assertEquals(-1, new FoodEntryProteinContentComparator().compare(f1, f2),
                "First foodEntry protein content is not greater than the second foodEntry's");
    }

    @Test
    void testCompareFirstProteinContentIsGreaterThanSecond() {
        NutritionInfo n1 = new NutritionInfo(20, 60, 20);
        NutritionInfo n2 = new NutritionInfo(60, 30, 10);

        FoodEntry f1 = new FoodEntry("food1", 400, n1);
        FoodEntry f2 = new FoodEntry("food2", 400, n2);

        assertEquals(1, new FoodEntryProteinContentComparator().compare(f1, f2),
                "Second foodEntry protein content is not greater than the first foodEntry's");
    }

    @Test
    void testCompareFirstProteinContentEqualsSecond() {
        NutritionInfo n1 = new NutritionInfo(30, 60, 10);
        NutritionInfo n2 = new NutritionInfo(30, 60, 10);

        FoodEntry f1 = new FoodEntry("food1", 400, n1);
        FoodEntry f2 = new FoodEntry("food2", 400, n2);

        assertEquals(0, new FoodEntryProteinContentComparator().compare(f1, f2),
                "First and second food entries' protein contents are not equal");
    }
}