package com.kc.springrestapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kc.springrestapi.entity.Recipe;
import com.kc.springrestapi.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class SpringRestApplication {

	public static void main(String[] args) {

		SpringApplication.run(SpringRestApplication.class, args);
	}


	@Bean
	@Autowired
	CommandLineRunner runner(RecipeService recipeService) {
		return args -> {
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<Recipe>> mapType = new TypeReference<List<Recipe>>() {
			};
			File file = new ClassPathResource("/json/data.json").getFile();
			try {
				List<Recipe> recipeList = mapper.readValue(file,
						mapType);
				recipeService.saveAll(recipeList);
				System.out.println("Recipes saved!");
			} catch (IOException e) {
				System.out.println("Unable to save recipes: " + e.getMessage());
			}
		};
	}
}

