package skaro.coffey.cookbook.recipe.export;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import skaro.coffey.cookbook.ingredient.Ingredient;
import skaro.coffey.cookbook.recipe.Recipe;

@Service
public class ITextExportService implements RecipeExportService {

	@Override
	public RecipeExport exportRecipe(Recipe recipe) throws DocumentException {
		Document document = new Document();
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		
		PdfWriter.getInstance(document, byteStream);
		document.open();
		addTitleHeader(document, recipe);
		addIngredients(document, recipe);
		addInstructions(document, recipe);
		
		document.close();
		return createExport(recipe, byteStream);
	}
	
	private void addTitleHeader(Document document, Recipe recipe) throws DocumentException {
		Font font = FontFactory.getFont(FontFactory.TIMES_BOLD, 20, BaseColor.BLACK);
        Paragraph para = new Paragraph(recipe.getLabel(), font);
        para.setAlignment(Element.ALIGN_CENTER);
        document.add(para);
        document.add(Chunk.NEWLINE);
	}

	private void addIngredients(Document document, Recipe recipe) throws DocumentException {
		List ingredientsList = new List(List.UNORDERED);
		Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, BaseColor.BLACK);
		recipe.getIngredients().stream()
			.sorted(Comparator.comparingInt(Ingredient::getSortOrder))
			.map(ingredient -> new ListItem(formatIngredient(ingredient), font))
			.forEach(formattedIngredient -> ingredientsList.add(formattedIngredient));
		
		document.add(ingredientsList);
		document.add(Chunk.NEWLINE);
	}
	
	private void addInstructions(Document document, Recipe recipe) throws DocumentException {
		Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, BaseColor.BLACK);
		
		java.util.List<Paragraph> paragraphs = Stream.of(recipe.getInstructions().split("/n"))
			.map(logicalParagraph -> new Paragraph(logicalParagraph, font))
			.collect(Collectors.toList());
		
		for(Paragraph paragraph : paragraphs) {
			document.add(paragraph);
		}
	}
	
	private RecipeExport createExport(Recipe recipe, ByteArrayOutputStream byteStream) {
		RecipeExport result = new RecipeExport();
		result.setRecipe(recipe);
		result.setCreatedDate(Calendar.getInstance());
		result.setExportFile(byteStream.toByteArray());
		return result;
	}
	
	private String formatIngredient(Ingredient ingredient) {
		return formatQuantity(ingredient) + formatUnits(ingredient) + ingredient.getLabel() + formatIsOptional(ingredient);
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
