package skaro.coffey.cookbook.tag;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import skaro.coffey.cookbook.security.AuthorityService;

@Component
public class TagProcessor implements RepresentationModelProcessor<EntityModel<Tag>> {
	public static final String UPDATE_LINK = "update";
	public static final String DELETE_LINK = "delete";
	
	private AuthorityService authService;
	
	public TagProcessor(AuthorityService authService) {
		this.authService = authService;
	}

	@Override
	public EntityModel<Tag> process(EntityModel<Tag> model) {
		addPermissionLinks(model);
		return model;
	}
	
	private void addPermissionLinks(EntityModel<Tag> model) {
		if(!authService.isNamedUser()) {
			return;
		}
		Link selfLink = model.getRequiredLink("self");
		Link updateLink = Link.of(selfLink.getHref(), UPDATE_LINK);
		Link deleteLink = Link.of(selfLink.getHref(), DELETE_LINK);
		
		model.add(updateLink, deleteLink);
	}
}
