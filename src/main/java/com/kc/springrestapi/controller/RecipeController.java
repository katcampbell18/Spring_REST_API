package com.kc.springrestapi.controller;

import com.kc.springrestapi.entity.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kc.springrestapi.service.RecipeService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @GetMapping("/detail/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable long id) {
        return ResponseEntity.ok().body(this.recipeService.getRecipeById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<Recipe> addRecipe(@RequestBody Recipe recipe) {
        Recipe newRecipe = recipeService.createRecipe(recipe);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newRecipe.getId())
                .toUri();
        return ResponseEntity.created(uri).body(newRecipe);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable long id, @RequestBody Recipe recipe) {
        recipe.setId(id);
        return ResponseEntity.ok().body(this.recipeService.updateRecipe(recipe));
    }

    @DeleteMapping("/delete/{id}")
    public HttpStatus deleteRecipe(@PathVariable long id) {
     this.recipeService.deleteRecipe(id);
     return HttpStatus.OK;
    }
}
