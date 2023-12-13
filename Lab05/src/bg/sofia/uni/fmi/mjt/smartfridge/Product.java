package bg.sofia.uni.fmi.mjt.smartfridge;

import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.Ingredient;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.Storable;

public class Product implements Ingredient {
    @Override
    public Storable item() {
        return null;
    }

    @Override
    public int quantity() {
        return 0;
    }
}
