package skaro.coffey.cookbook.tag;

import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.Description;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource
public interface TagRepository extends PagingAndSortingRepository<Tag, String> {
	@RestResource(exported = true, path = "searchByPartialLabel", rel = "searchByPartialLabel", description = @Description("Find tag where parameter is contained in label. Ignores case."))
	Set<Tag> findByLabelContainingIgnoreCase(@Param("partialLabel") String partialLabel);
}
