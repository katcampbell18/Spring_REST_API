package com.kc.demo;

import com.kc.demo.entity.Recipe;
import com.kc.demo.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RecipeTest {

    @Autowired
    RecipeRepository recipeRepo;

    @Test
    public void createRecipeTest() {
        Recipe r = new Recipe();
        Set<String> ingredients_set = Stream.of("2 slices of American cheese", "2 slices of white bread", "2 tsp butter").collect(Collectors.toSet());
        Set<String> instructions_set = Stream.of("Preheat a cast-iron skillet.", "Spread 1 tsp of butter onto one side of eah bread slice",
                "Place 1 bread slice buttered side down in the skillet.", "Top with cheese and the second bread slice buttered side up.",
                "Flip carefully using a wide spatula, then cook for 2 more minutes on the second side; the bread should be barley colored.",
                "Repeat, cooking for about 2 minutes more on each side.", "Cut in half and serve immediately.").collect(Collectors.toSet());
        r.setId(1L);
        r.setName("grilled cheese sandwich");
        r.setIngredients(ingredients_set);
        r.setInstructions(instructions_set);
        recipeRepo.save(r);
        assertNotNull(recipeRepo.findById(1L).get());
    }
}
