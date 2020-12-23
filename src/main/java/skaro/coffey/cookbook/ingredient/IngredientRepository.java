package skaro.coffey.cookbook.ingredient;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource
public interface IngredientRepository extends PagingAndSortingRepository<Ingredient, String> {

}
