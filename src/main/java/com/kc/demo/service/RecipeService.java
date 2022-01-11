package com.kc.demo.service;

import com.kc.demo.entity.Recipe;
import com.kc.demo.utils.RecipeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kc.demo.repository.RecipeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {
    @Autowired
    RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public void saveAll(List<Recipe> recipes) {
        recipeRepository.saveAll(recipes);
    }

    public List<Recipe> getRecipes() {
        List<Recipe> recipesList = new ArrayList<>();
        Iterable<Recipe> recipes = recipeRepository.findAll();
        recipes.forEach(recipesList::add);
        return recipesList;
    }

    public Recipe getRecipeById(long recipeId) {
        Optional<Recipe> recipeDb = this.recipeRepository.findById(recipeId);
        if(recipeDb.isPresent()) {
            return recipeDb.get();
        }else {
        try {
            throw new RecipeNotFoundException("Recipe not found with id: " + recipeId);
        } catch (RecipeNotFoundException e) {
            e.printStackTrace();
        }
    }
        return null;
    }

    public Recipe createRecipe(Recipe recipe) {
        Recipe newRecipe = new Recipe(
                recipe.getName(),
                recipe.getIngredients(),
                recipe.getInstructions()
        );
        return recipeRepository.save(newRecipe);
    }

    public Recipe updateRecipe(Recipe recipe) {
        Optional<Recipe> recipeDb = this.recipeRepository.findById(recipe.getId());
        if(recipeDb.isPresent()) {
            Recipe recipeUpdate = recipeDb.get();
            recipeUpdate.setId(recipe.getId());
            recipeUpdate.setName(recipe.getName());
            recipeUpdate.setIngredients(recipe.getIngredients());
            recipeUpdate.setInstructions(recipe.getInstructions());
            recipeRepository.save(recipeUpdate);
            return recipeUpdate;
        }else {
            try {
                throw new RecipeNotFoundException("Recipe not found with id: " + recipe.getId());
            } catch (RecipeNotFoundException e) {
                e.printStackTrace();
            }
        }
        return recipe;
    }

    public void deleteRecipe(Long recipeId) {
        Optional<Recipe> recipeDb = this.recipeRepository.findById(recipeId);
        if(recipeDb.isPresent()){
            recipeRepository.delete(recipeDb.get());
        } else{
            try {
                throw new RecipeNotFoundException("Recipe not found with id: " + recipeId);
            } catch (RecipeNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
