package skaro.coffey.cookbook.recipe.export;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
//		TODO: There is currently a bug that does not set the base path for links correctly for the WebMvcLinkBuilder way creating a link. 
//		Once this bug is fixed, change the self link instantiation to the code blow. Link: https://github.com/spring-projects/spring-data-rest/issues/1342
//		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RecipeExportController.class).getExport(export.getRecipe().getId())).withSelfRel();
		
		Link selfLink = Link.of(ServletUriComponentsBuilder.fromCurrentRequest().toUriString()).withSelfRel();
		Link recipeLink = entityLinks.linkToItemResource(export.getRecipe(), Recipe::getId);
		EntityModel<RecipeExport> resource = EntityModel.of(export, selfLink, recipeLink);
		
		return resource;
	}
	
}
