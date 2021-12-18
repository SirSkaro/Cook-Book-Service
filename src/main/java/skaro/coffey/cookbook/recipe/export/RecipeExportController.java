package skaro.coffey.cookbook.recipe.export;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.itextpdf.text.DocumentException;

import skaro.coffey.cookbook.recipe.Recipe;
import skaro.coffey.cookbook.recipe.RecipeRepository;

@RepositoryRestController
public class RecipeExportController {
	private static final String EXPORT_PATH = "/" + RecipeRepository.PATH + "/{id}/export";
	
	private RecipeExportService exportService;
	private RecipeRepository recipeRepository;
	private RepositoryEntityLinks entityLinks;
	
	@Autowired
	public RecipeExportController(RecipeExportService exportService, RecipeRepository recipeRepository, RepositoryEntityLinks entityLinks) {
		this.exportService = exportService;
		this.recipeRepository = recipeRepository;
		this.entityLinks = entityLinks;
	}
	
	@GetMapping(EXPORT_PATH)
	public ResponseEntity<?> getExport(@PathVariable String id) {
		return recipeRepository.findById(id)
				.map(this::formatResponse)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	private ResponseEntity<?> formatResponse(Recipe recipe) {
		try {
			RecipeExport export = exportService.exportRecipe(recipe);
			return ResponseEntity.ok(toResource(export));
		} catch(IOException | DocumentException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	private EntityModel<RecipeExport> toResource(RecipeExport export) {
		Link selfLink = linkTo(methodOn(RecipeExportController.class).getExport(export.getRecipe().getId())).withSelfRel();
		Link recipeLink = entityLinks.linkToItemResource(export.getRecipe(), Recipe::getId);
		EntityModel<RecipeExport> resource = EntityModel.of(export, selfLink, recipeLink);
		
		return resource;
	}
	
}
