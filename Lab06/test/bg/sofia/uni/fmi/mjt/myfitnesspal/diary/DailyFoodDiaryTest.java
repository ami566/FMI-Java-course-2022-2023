package bg.sofia.uni.fmi.mjt.myfitnesspal.diary;

import bg.sofia.uni.fmi.mjt.myfitnesspal.exception.UnknownFoodException;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfoAPI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DailyFoodDiaryTest {
    private static final String FOOD1 = "eggs";
    private static final String FOOD2 = "croissant";
    private static final String FOOD3 = "fries";
    private static final String FOOD4 = "pasta";
    private static final NutritionInfo N1 = new NutritionInfo(50, 20, 30);
    private static final NutritionInfo N2 = new NutritionInfo(50, 30, 20);
    @Mock
    private NutritionInfoAPI nutritionInfoAPIMock;

    @InjectMocks
    private DailyFoodDiary dailyFoodDiary;

    @Test
    void testDailyFoodDiaryAddFoodWithMealNull() {
        assertThrows(IllegalArgumentException.class,
                () -> dailyFoodDiary.addFood(null, FOOD1, 100.0),
                "Meal cannot be null");
    }

    @Test
    void testDailyFoodDiaryAddFoodWithFoodNameNull() {
        assertThrows(IllegalArgumentException.class,
                () -> dailyFoodDiary.addFood(Meal.BREAKFAST, null, 100.0),
                "FoodName cannot be null");
    }

    @Test
    void testDailyFoodDiaryAddFoodWithFoodNameBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> dailyFoodDiary.addFood(Meal.BREAKFAST, "", 100.0),
                "FoodName cannot be blank");
    }

    @Test
    void testDailyFoodDiaryAddFoodWithServingSizeNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> dailyFoodDiary.addFood(Meal.BREAKFAST, FOOD1, -7),
                "Serving cannot be negative");
    }

    @Test
    void testDailyFoodDiaryAddFoodWithNoNutritionInfo() throws UnknownFoodException {
        when(nutritionInfoAPIMock.getNutritionInfo(FOOD1)).
                thenThrow(new UnknownFoodException("Food has no nutrition info"));

        assertThrows(UnknownFoodException.class, () -> dailyFoodDiary.addFood(Meal.BREAKFAST, FOOD1, 10.0),
                "UnknownFoodException should be thrown");
    }


    @Test
    void testDailyFoodDiaryCaloriesIntakePerMealMealNull() {
        assertThrows(IllegalArgumentException.class, () -> dailyFoodDiary.getDailyCaloriesIntakePerMeal(null),
                "Meal cannot be null");
    }

    @Test
    void testDailyIntakePerMealDiff() throws UnknownFoodException {

        when(nutritionInfoAPIMock.getNutritionInfo(FOOD1)).thenReturn(N1);
        when(nutritionInfoAPIMock.getNutritionInfo(FOOD2)).thenReturn(N2);

        dailyFoodDiary.addFood(Meal.BREAKFAST, FOOD1, 2);
        dailyFoodDiary.addFood(Meal.BREAKFAST, FOOD2, 1);

        double calc = (80 * 4 + 20 * 9) * 2 + 70 * 4 + 30 * 9;
        assertEquals(calc, dailyFoodDiary.getDailyCaloriesIntakePerMeal(Meal.BREAKFAST),
                "Intake per meal returns wrong answer");
    }

    @Test
    void testDailyIntakeCaloriesDiff() throws UnknownFoodException {

        when(nutritionInfoAPIMock.getNutritionInfo(FOOD1)).thenReturn(N1);
        when(nutritionInfoAPIMock.getNutritionInfo(FOOD2)).thenReturn(N2);
        when(nutritionInfoAPIMock.getNutritionInfo(FOOD3)).thenReturn(N1);
        when(nutritionInfoAPIMock.getNutritionInfo(FOOD4)).thenReturn(N2);

        dailyFoodDiary.addFood(Meal.BREAKFAST, FOOD1, 2);
        dailyFoodDiary.addFood(Meal.SNACKS, FOOD2, 1);
        dailyFoodDiary.addFood(Meal.LUNCH, FOOD3, 2);
        dailyFoodDiary.addFood(Meal.DINNER, FOOD4, 3);

        double calc = (80 * 4 + 20 * 9) * 4 + (70 * 4 + 30 * 9) * 4;
        assertEquals(calc, dailyFoodDiary.getDailyCaloriesIntake(),
                "Calories intake returns wrong answer");
    }

    @Test
    void testGetAllFoodEntriesValid() throws UnknownFoodException {

        when(nutritionInfoAPIMock.getNutritionInfo(FOOD1)).thenReturn(N1);
        when(nutritionInfoAPIMock.getNutritionInfo(FOOD2)).thenReturn(N2);
        when(nutritionInfoAPIMock.getNutritionInfo(FOOD3)).thenReturn(N1);
        when(nutritionInfoAPIMock.getNutritionInfo(FOOD4)).thenReturn(N2);


        dailyFoodDiary.addFood(Meal.BREAKFAST, FOOD1, 2);
        dailyFoodDiary.addFood(Meal.SNACKS, FOOD2, 1);
        dailyFoodDiary.addFood(Meal.LUNCH, FOOD3, 2);
        dailyFoodDiary.addFood(Meal.DINNER, FOOD4, 3);

        List<FoodEntry> list = new ArrayList<>();
        list.add(new FoodEntry(FOOD1, 2, N1));
        list.add(new FoodEntry(FOOD2, 1, N2));
        list.add(new FoodEntry(FOOD3, 2, N1));
        list.add(new FoodEntry(FOOD4, 3, N2));

        assertEquals(list.size(), dailyFoodDiary.getAllFoodEntries().size(),
                "Problem with getting all food entries");
    }
}