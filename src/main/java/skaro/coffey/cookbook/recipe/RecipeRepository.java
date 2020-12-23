package skaro.coffey.cookbook.recipe;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource
public interface RecipeRepository extends PagingAndSortingRepository<Recipe, String> {

}
