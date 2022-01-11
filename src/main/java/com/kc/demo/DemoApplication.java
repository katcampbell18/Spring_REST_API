package com.kc.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kc.demo.entity.Recipe;
import com.kc.demo.service.RecipeService;
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
//@EnableJpaRepositories("com.kc.repository")
public class DemoApplication {

	public static void main(String[] args) throws IOException {

		SpringApplication.run(DemoApplication.class, args);
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

