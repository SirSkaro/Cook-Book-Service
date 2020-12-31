package skaro.coffey.cookbook.recipe.export;

import java.util.Calendar;

import skaro.coffey.cookbook.recipe.Recipe;

public class RecipeExport {
	private Calendar createdDate;
	private byte[] exportFile;
	private Recipe recipe;
	
	public Calendar getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}
	public byte[] getExportFile() {
		return exportFile;
	}
	public void setExportFile(byte[] exportFile) {
		this.exportFile = exportFile;
	}
	public Recipe getRecipe() {
		return recipe;
	}
	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}
}
