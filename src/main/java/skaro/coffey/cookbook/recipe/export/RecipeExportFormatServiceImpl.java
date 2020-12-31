package skaro.coffey.cookbook.recipe.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.text.WordUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import skaro.coffey.cookbook.ingredient.Ingredient;
import skaro.coffey.cookbook.recipe.Recipe;

@Service
public class RecipeExportFormatServiceImpl implements RecipeExportService {

	@Override
	public RecipeExport exportRecipe(Recipe recipe) throws IOException {
		PDDocument document = formatExport(recipe);
		ByteArrayOutputStream byteStream = toByteStream(document);
		return createExport(recipe, byteStream);
	}

	private PDDocument formatExport(Recipe recipe) throws IOException {
		PDDocument result = new PDDocument();
		PDPage page = new PDPage();
		result.addPage(page);

		PDPageContentStream contentStream = new PDPageContentStream(result, page);
		contentStream.setFont(PDType1Font.TIMES_ROMAN, 14);
		
		contentStream.beginText();
		contentStream.newLineAtOffset(25, 725);
		contentStream.showText(recipe.getLabel());
		contentStream.newLine();
		contentStream.newLine();
		formatIngredients(recipe, contentStream);
		contentStream.newLine();
		contentStream.newLine();
		formatInstructions(recipe, contentStream);
		
		contentStream.endText();
		contentStream.close();
		return result;
	}
	
	private ByteArrayOutputStream toByteStream(PDDocument document) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		document.save(byteStream);
		document.close();
		return byteStream;
	}
	
	private RecipeExport createExport(Recipe recipe, ByteArrayOutputStream byteStream) {
		RecipeExport result = new RecipeExport();
		result.setRecipe(recipe);
		result.setCreatedDate(Calendar.getInstance());
		result.setExportFile(byteStream.toByteArray());
		return result;
	}
	
	private void formatIngredients(Recipe recipe, PDPageContentStream contentStream) throws IOException {
		for(Ingredient ingredient : recipe.getIngredients()) {
			String formattedIngredient = formatQuantity(ingredient) + formatUnits(ingredient) + ingredient.getLabel() + formatIsOptional(ingredient);
			contentStream.showText(formattedIngredient);
			contentStream.newLine();
		}
	}
	
	private void formatInstructions(Recipe recipe, PDPageContentStream contentStream) throws IOException {
		List<String> wrappedLines = Stream.of(WordUtils.wrap(recipe.getInstructions(), 100).split("\\r\\n"))
				.collect(Collectors.toList());
		
		for(String instructionLine : wrappedLines) {
			contentStream.showText(instructionLine.replace("\n", "<br>"));
			contentStream.newLine();
		}
	}
	
	private String formatQuantity(Ingredient ingredient) {
		if(ingredient.getQuantityMin() == null) {
			return "";
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(ingredient.getQuantityMin());
		if(ingredient.getQuantityMax() != null) {
			stringBuilder.append(" - ").append(ingredient.getQuantityMax());
		}
		
		return stringBuilder.append(" ").toString();
	}
	
	private String formatUnits(Ingredient ingredient) {
		return ingredient.getUnits() != null ? ingredient.getUnits() + " " : "";
	}
	
	private String formatIsOptional(Ingredient ingredient) {
		return ingredient.getOptional() ? " (optional)" : "";
	}
}
