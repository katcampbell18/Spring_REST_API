package com.kc.springrestapi;

import com.kc.springrestapi.entity.Recipe;
import com.kc.springrestapi.repository.RecipeRepository;
import com.kc.springrestapi.service.RecipeService;
import com.kc.springrestapi.utils.RecipeAlreadyExistsException;
import com.kc.springrestapi.utils.RecipeNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;
    private Recipe recipe1;
    private Recipe recipe2;
    List<Recipe> recipeList;

    @BeforeEach
    void initUseCase() {
        recipeService = new RecipeService(recipeRepository);
        recipeList = new ArrayList<>();
        recipe1 = new Recipe("grilled cheese sandwich", Stream.of("2 slices of American cheese", "2 slices of white bread",
                "2 tsp butter").collect(Collectors.toSet()), Stream.of("Preheat a cast-iron skillet.",
                "Spread 1 tsp of butter onto one side of each bread slice", "Place 1 bread slice buttered side down in the skillet.",
                "Top with cheese and the second bread slice buttered side up.",
                "Flip carefully using a wide spatula, then cook for 2 more minutes on the second side; the bread should be barely colored.",
                "Repeat, cooking for about 2 minutes more on each side.", "Cut in half and serve immediately.")
                .collect(Collectors.toSet()));
        recipe2 = new Recipe("peanut butter and jelly sandwich", Stream.of("2 slices of white bread", "2 tsp peanut butter",
                "2 tsp grape jelly").collect(Collectors.toSet()), Stream.of("Spread 2 tsp of peanut butter onto one side of a bread slice.",
                "Spread 2 tsp of grape jelly on the second bread slice.",
                "Place the second slice (jelly side down) on top of the first.",
                "Cut in half and serve immediately.").collect(Collectors.toSet()));
        recipeList.add(recipe1);
        recipeList.add(recipe2);
    }

    @AfterEach
    public void tearDown() {
        recipe1 = recipe2 = null;
        recipeList = null;
    }

    @Test
    public void recipesExistInDb_SuccessTest() {
        recipeRepository.save(recipe1);
        when(recipeRepository.findAll()).thenReturn(recipeList);

        List<Recipe> fetchedRecipes = recipeService.getRecipes();

        assertEquals(fetchedRecipes, recipeList);
        verify(recipeRepository, times(1)).save(recipe1);
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    public void recipeSearchById_SuccessTest() {
        Long id = 1L;
        recipe1.setId(id);
        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe1));

        assertThat(recipeService.getRecipeById(recipe1.getId())).isEqualTo(recipe1);
    }

    @Test
    public void createRecipe_SuccessTest() {
        recipe1.setId(1L);
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe1);

        Recipe savedRecipe = recipeService.createRecipe(recipe1);

        assertThat(savedRecipe.getName()).isNotNull();
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }

    @Test
    public void updateRecipe_SuccessTest() {
        Long id = 2L;
        recipe2.setId(id);
        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe2));

        recipe2.setName("classic peanut butter and jelly sandwich");
        when(recipeRepository.save(recipe2)).thenReturn(recipe2);

        assertThat(recipeService.updateRecipe(recipe2)).isEqualTo(recipe2);
    }

    @Test
    public void deleteRecipe_SuccessTest() {
        Long id = 2L;
        recipe2.setId(id);
        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe2));

        recipeService.deleteRecipe(recipe2.getId());

        assertFalse(recipeRepository.existsById(recipe2.getId()));
    }

    @Test
    public void recipeIdAlreadyExists_ExceptionTest() {
        recipe1.setId(1L);
        recipeRepository.save(recipe1);
        recipe2.setId(1L);
        when(recipeRepository.existsById(recipe1.getId())).thenThrow(new RecipeAlreadyExistsException(
                "Recipe already exists with id: " + recipe2.getId()));

        Exception exception = assertThrows(RecipeAlreadyExistsException.class, () ->
                recipeService.createRecipe(recipe2));
        assertEquals("Recipe already exists with id: 1", exception.getMessage());
    }

    @Test
    public void recipeNotFoundById_ExceptionTest() {
        Long recipeId = 3L;
        when(recipeService.getRecipeById(recipeId)).thenThrow(new RecipeNotFoundException(
                "Recipe not found with id: " + recipeId));

        Exception exception = assertThrows(RecipeNotFoundException.class, () ->
                recipeService.getRecipeById(recipeId));
        assertEquals("Recipe not found with id: 3", exception.getMessage());
    }
}
