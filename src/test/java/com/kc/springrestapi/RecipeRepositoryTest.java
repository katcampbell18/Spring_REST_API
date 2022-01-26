package com.kc.springrestapi;

import com.kc.springrestapi.entity.Recipe;
import com.kc.springrestapi.repository.RecipeRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RecipeRepositoryTest {

    @Autowired
    RecipeRepository recipeRepo;

    @Test
    @Order(1)
    public void createRecipeTest() {
        Recipe r = new Recipe();
        r.setId(1L);
        r.setName("grilled cheese sandwich");
        r.setIngredients(Stream.of("2 slices of American cheese", "2 slices of white bread", "2 tsp butter").collect(Collectors.toSet()));
        r.setInstructions(Stream.of("Preheat a cast-iron skillet.", "Spread 1 tsp of butter onto one side of each bread slice",
                "Place 1 bread slice buttered side down in the skillet.", "Top with cheese and the second bread slice buttered side up.",
                "Flip carefully using a wide spatula, then cook for 2 more minutes on the second side; the bread should be barley colored.",
                "Repeat, cooking for about 2 minutes more on each side.", "Cut in half and serve immediately.").collect(Collectors.toSet()));
        recipeRepo.save(r);
        assertNotNull(recipeRepo.findById(1L).get());
    }

    @Test
    @Order(2)
    public void readAllRecipesTest() {
        List newList = new ArrayList();
        Iterable<Recipe> list = recipeRepo.findAll();
        list.forEach(newList::add);
        assertFalse(newList.isEmpty());
    }

    @Test
    @Order(3)
    public void findRecipeByIdTest() {
        Recipe recipe = recipeRepo.findById(1L).get();
        assertEquals("grilled cheese sandwich", recipe.getName());
    }

    @Test
    @Order(4)
    public void updateRecipeTest() {
        Recipe recipe = recipeRepo.findById(1L).get();
        recipe.setName("basic grilled cheese sandwich");
        recipeRepo.save(recipe);
        assertNotEquals("grilled cheese sandwich", recipeRepo.findById(1L).get().getName());
    }

    @Test
    @Order(5)
    public void deleteRecipeTest() {
        recipeRepo.deleteById(1L);
        assertFalse(recipeRepo.existsById(1L));
    }
}
