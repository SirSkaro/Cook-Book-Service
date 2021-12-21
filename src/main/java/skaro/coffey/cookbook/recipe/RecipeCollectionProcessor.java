package skaro.coffey.cookbook.recipe;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import skaro.coffey.cookbook.security.AuthorityService;

@Component
public class RecipeCollectionProcessor implements RepresentationModelProcessor<PagedModel<EntityModel<Recipe>>> {
	public static final String CREATE_LINK = "create";
	
	private AuthorityService authService;
	
	public RecipeCollectionProcessor(AuthorityService authService) {
		this.authService = authService;
	}

	@Override
	public PagedModel<EntityModel<Recipe>> process(PagedModel<EntityModel<Recipe>> model) {
		if(!authService.isNamedUser()) {
			return model;
		}
		Link selfLink = model.getRequiredLink("self");
		Link createLink = Link.of(selfLink.getHref(), CREATE_LINK);
		
		model.add(createLink);
		return model;
	}

}
