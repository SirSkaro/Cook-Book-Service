package skaro.coffey.cookbook.recipe.export;

import java.io.IOException;

import skaro.coffey.cookbook.recipe.Recipe;

public interface RecipeExportService {
	RecipeExport exportRecipe(Recipe recipe) throws IOException;
}
