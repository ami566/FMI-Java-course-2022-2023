package bg.sofia.uni.fmi.mjt.cocktail.server.storage;

import bg.sofia.uni.fmi.mjt.cocktail.server.Cocktail;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailNotFoundException;

import java.util.*;

public class DefaultCocktailStorage implements CocktailStorage {
    Map<String, Cocktail> recipes;

    public DefaultCocktailStorage() {
        recipes = new HashMap<>();
    }

    @Override
    public void createCocktail(Cocktail cocktail) throws CocktailAlreadyExistsException {
        if (recipes.containsKey(cocktail.name())) {
            throw new CocktailAlreadyExistsException("Recipe for " + cocktail.name() + " already exists");
        }
        recipes.put(cocktail.name(), cocktail);
    }

    @Override
    public Collection<Cocktail> getCocktails() {
        return Collections.unmodifiableCollection(recipes.values());
    }

    @Override
    public Collection<Cocktail> getCocktailsWithIngredient(String ingredientName) {
        return recipes.values().stream().filter(drink -> drink.containsIngredient(ingredientName))
                .toList();
    }

    @Override
    public Cocktail getCocktail(String name) throws CocktailNotFoundException {

        if (!recipes.containsKey(name)) {
            throw new CocktailNotFoundException("Recipe for " + name + " is not found");
        }
        return recipes.get(name);
    }
}
