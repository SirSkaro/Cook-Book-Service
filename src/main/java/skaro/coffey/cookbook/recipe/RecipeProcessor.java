package skaro.coffey.cookbook.recipe;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class RecipeProcessor implements RepresentationModelProcessor<EntityModel<Recipe>> {
	public static final String EXPORT_LINK = "export";
	
	@Override
	public EntityModel<Recipe> process(EntityModel<Recipe> model) {
		addExportLink(model);
		return model;
	}

	private void addExportLink(EntityModel<Recipe> model) {
//		TODO: There is currently a bug that does not set the base path for links correctly for the WebMvcLinkBuilder way of creating a link. 
//		Once this bug is fixed, change the link instantiation to the code blow. Link: https://github.com/spring-projects/spring-data-rest/issues/1342
//		Link exportLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RecipeExportController.class).getExport(model.getContent().getId())).withRel(EXPORT_LINK);
		Link exportLink = Link.of(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + EXPORT_LINK).toUriString()).withRel(EXPORT_LINK);
		model.add(exportLink);
	}
}
