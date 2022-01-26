package com.kc.springrestapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kc.springrestapi.controller.RecipeController;
import com.kc.springrestapi.entity.Recipe;
import com.kc.springrestapi.repository.RecipeRepository;
import com.kc.springrestapi.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@WebMvcTest(RecipeController.class)
public class RecipeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    RecipeRepository recipeRepository;

    @MockBean
    RecipeService recipeService;

    Recipe RECIPE_1 = new Recipe(1L, "grilled cheese sandwich",
            Stream.of("2 slices of American cheese", "2 slices of white bread", "2 tsp butter").collect(Collectors.toSet()),
            Stream.of("Preheat a cast-iron skillet.", "Spread 1 tsp of butter onto one side of each bread slice",
                    "Place 1 bread slice buttered side down in the skillet.", "Top with cheese and the second bread slice buttered side up.",
                    "Flip carefully using a wide spatula, then cook for 2 more minutes on the second side; the bread should be barely colored.",
                    "Repeat, cooking for about 2 minutes more on each side.", "Cut in half and serve immediately.")
                    .collect(Collectors.toSet()));

    Recipe RECIPE_2 = new Recipe(2L, "peanut butter and jelly sandwich",
            Stream.of("2 slices of white bread", "peanut butter", "grape jelly").collect(Collectors.toSet()),
            Stream.of("Spread peanut butter onto one side of a bread slice.",
                    "Spread grape jelly onto one side of the other bread slice.", "Put the bread slices together.",
                    "Cut in half and serve immediately.").collect(Collectors.toSet()));

    @Test
    public void getAllRecipes_SuccessTest() throws Exception {
        List<Recipe> recipes = new ArrayList<>(Arrays.asList(RECIPE_1, RECIPE_2));

        Mockito.when(recipeService.getRecipes()).thenReturn(recipes);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("[0].name", is("grilled cheese sandwich")));
    }

    @Test
    public void getRecipeById_SuccessTest() throws Exception {
        Mockito.when(recipeService.getRecipeById(RECIPE_1.getId())).thenReturn(RECIPE_1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/detail/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("peanut butter and jelly sandwich")));
    }

    @Test
    public void createRecipe_SuccessTest() throws Exception {
        Recipe recipe = new Recipe();
            recipe.setId(3L);
                recipe.setName("hot chocolate");
                recipe.setIngredients(Stream.of("2 cups of milk", "1 Tbsp sugar", "1/2 cup chocolate chips",
                                "1/8 tsp vanilla extract").collect(Collectors.toSet()));
                recipe.setInstructions(Stream.of("Heat 2 cups of milk and in a sauce pan. Make sure milk does not boil.",
                        "Pour in chocolate chips and sugar.", "Stir until the chocolate chips are melted.",
                        "Remove from heat and add the vanilla extract.",
                        "Pour into cups and enjoy!").collect(Collectors.toSet()));

        Mockito.when(recipeService.createRecipe(recipe)).thenReturn(recipe);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(recipe));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("hot chocolate")));
    }

    @Test
    public void updateRecipe_SuccessTest() throws Exception {
        Mockito.when(recipeRepository.findById(RECIPE_2.getId())).thenReturn(Optional.of(RECIPE_2));

        Recipe updatedRecipe = recipeRepository.findById(RECIPE_2.getId()).get();
        updatedRecipe.setName("classic peanut butter and jelly sandwich");
        Mockito.when(recipeService.updateRecipe(ArgumentMatchers.any())).thenReturn(updatedRecipe);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/update/2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedRecipe));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("classic peanut butter and jelly sandwich")));
    }

    @Test
    public void deleteRecipe_SuccessTest() throws Exception {
        Mockito.when(recipeService.getRecipeById(RECIPE_2.getId())).thenReturn(RECIPE_2);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/delete/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}