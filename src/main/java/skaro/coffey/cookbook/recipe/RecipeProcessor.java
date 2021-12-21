package skaro.coffey.cookbook.recipe;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import skaro.coffey.cookbook.recipe.export.RecipeExportController;
import skaro.coffey.cookbook.security.AuthorityService;

@Component
public class RecipeProcessor implements RepresentationModelProcessor<EntityModel<Recipe>> {
	public static final String EXPORT_LINK = "export";
	public static final String UPDATE_LINK = "update";
	public static final String DELETE_LINK = "delete";
	
	private AuthorityService authService;
	
	public RecipeProcessor(AuthorityService authService) {
		this.authService = authService;
	}

	@Override
	public EntityModel<Recipe> process(EntityModel<Recipe> model) {
		addExportLink(model);
		addPermissionLinks(model);
		return model;
	}

	private void addExportLink(EntityModel<Recipe> model) {
		Link exportLink = linkTo(methodOn(RecipeExportController.class).getExport(model.getContent().getId()))
				.withRel(EXPORT_LINK);
		model.add(exportLink);
	}
	
	private void addPermissionLinks(EntityModel<Recipe> model) {
		if(!authService.isNamedUser()) {
			return;
		}
		Link selfLink = model.getRequiredLink("self");
		Link updateLink = Link.of(selfLink.getHref(), UPDATE_LINK);
		Link deleteLink = Link.of(selfLink.getHref(), DELETE_LINK);
		
		model.add(updateLink, deleteLink);
	}
}
