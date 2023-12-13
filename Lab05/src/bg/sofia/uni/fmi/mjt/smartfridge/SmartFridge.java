package bg.sofia.uni.fmi.mjt.smartfridge;

import bg.sofia.uni.fmi.mjt.smartfridge.exception.FridgeCapacityExceededException;
import bg.sofia.uni.fmi.mjt.smartfridge.exception.InsufficientQuantityException;
import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.DefaultIngredient;
import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.Ingredient;
import bg.sofia.uni.fmi.mjt.smartfridge.recipe.Recipe;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.Storable;

import java.util.*;

public class SmartFridge implements SmartFridgeAPI {
    private int totalCapacity;
    private HashMap<Storable, Integer> products;
    private int currentItemsCount;

    SmartFridge(int totalCapacity) {
        this.totalCapacity = totalCapacity;
        products = new HashMap<>();
        currentItemsCount = 0;
    }

    @Override
    public <E extends Storable> void store(E item, int quantity) throws FridgeCapacityExceededException {
        if (item == null || quantity <= 0) {
            throw new IllegalArgumentException("Item cannot be null and quantity should be positive");
        }

        if (currentItemsCount + quantity > totalCapacity) {
            throw new FridgeCapacityExceededException("There is no free space to accommodate the items");
        }

        if (products.containsKey(item)) {
            products.replace(item, products.get(item), products.get(item) + quantity);
        } else {
            products.put(item, quantity);
        }
        currentItemsCount += quantity;
    }

    private Storable getItemWithName(String name) {
        Storable s = null;
        for (Map.Entry<Storable, Integer> item : products.entrySet()) {
            s = item.getKey();
            if (s.getName().equals(name)) {
                return s;
            }
        }
        return null;
    }

    @Override
    public List<? extends Storable> retrieve(String itemName) {
        if (itemName == null || itemName.isEmpty() || itemName.isBlank()) {
            throw new IllegalArgumentException("ItemName should not be null, empty or blank");
        }


        Storable s = getItemWithName(itemName);

        List<Storable> list = new ArrayList<>();

        if (s == null) {
            return list;
        }

        int quant = products.get(s);
        for (int i = 0; i < quant; i++) {
            list.add(s);
        }

        products.remove(s);

        // sort

        return list;
    }

    @Override
    public List<? extends Storable> retrieve(String itemName, int quantity) throws InsufficientQuantityException {
        if (itemName == null || itemName.isEmpty() || itemName.isBlank()) {
            throw new IllegalArgumentException("ItemName should not be null, empty or blank");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity should be positive");
        }

        Storable s = getItemWithName(itemName);

        if (s == null) {
            throw new InsufficientQuantityException("Product not found");
        }
        int sQuantity = products.get(s);

        if (quantity > sQuantity) {
            throw new InsufficientQuantityException("The quantity is insufficient");

        }

        List<Storable> list = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            list.add(s);
        }

        if (sQuantity == quantity) {
            products.remove(s);
        } else {
            products.replace(s, sQuantity, sQuantity - quantity);
        }
        // sort
        return list;
    }

    @Override
    public int getQuantityOfItem(String itemName) {
        if (itemName == null || itemName.isEmpty() || itemName.isBlank()) {
            throw new IllegalArgumentException("ItemName should not be null, empty or blank");
        }

        Storable s = getItemWithName(itemName);
        if (s == null) {
            return 0;
        }
        return products.get(s);
    }

    @Override
    public Iterator<Ingredient<? extends Storable>> getMissingIngredientsFromRecipe(Recipe recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }

        Set<Ingredient<? extends Storable>> ingredients = recipe.getIngredients();
        Set<Ingredient<? extends Storable>> neededProducts = new HashSet<>();
        Iterator<Ingredient<? extends Storable>> iter = ingredients.iterator();
        ingredients.remove(iter);
        for (Ingredient<? extends Storable> it : ingredients) {
            int ingNeeded = it.quantity();
            for (Map.Entry<Storable, Integer> item : products.entrySet()) {
                if (item.getKey().getName().equals(it.item().getName()) && !item.getKey().isExpired()) {
                    if (ingNeeded > 0) {
                        ingNeeded -= item.getValue();
                    }
                }
            }

            if (ingNeeded >= 0) {
                neededProducts.add(new DefaultIngredient<>(it.item(), ingNeeded));
            }
        }
        return neededProducts.iterator();
    }

    @Override
    public List<? extends Storable> removeExpired() {
        List<Storable> list = new ArrayList<>();
        List<Storable> toRemove = new ArrayList<>();

        for (Map.Entry<Storable, Integer> item : products.entrySet()) {
            Storable s = item.getKey();
            if (s.isExpired()) {
                toRemove.add(s);
                for (int i = 0; i < item.getValue() - 1; i++) {
                    list.add(s);
                    currentItemsCount--;
                }
            }
        }

        for (Storable s : toRemove) {
            products.remove(s);
        }

        return list;
    }
}
