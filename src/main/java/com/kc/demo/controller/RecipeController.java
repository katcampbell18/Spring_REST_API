package com.kc.demo.controller;

import com.kc.demo.entity.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kc.demo.service.RecipeService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/recipes")
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return ResponseEntity.ok().body(this.recipeService.getRecipes());
    }

    @GetMapping("/recipes/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable long id) {
        return ResponseEntity.ok().body(this.recipeService.getRecipeById(id));
    }

    @PutMapping("/recipes/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable long id, @RequestBody Recipe recipe) {
        recipe.setId(id);
        return ResponseEntity.ok().body(this.recipeService.updateRecipe(recipe));
    }

    @DeleteMapping("/recipes/{id}")
    public HttpStatus deleteRecipe(@PathVariable long id) {
     this.recipeService.deleteRecipe(id);
     return HttpStatus.OK;
    }
}
