package skaro.coffey.cookbook.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.MultiValueBinding;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

@Repository
@RepositoryRestResource(path = RecipeRepository.PATH)
public interface RecipeRepository extends PagingAndSortingRepository<Recipe, String>, QuerydslPredicateExecutor<Recipe>, QuerydslBinderCustomizer<QRecipe> {
	public static final String PATH = "recipes";
	
	@Override
	default public void customize(QuerydslBindings bindings, QRecipe root) {
		bindings.bind(String.class).first((StringPath path, String value) -> {
			BooleanBuilder containsAllItemsInListPredicate = new BooleanBuilder();
			Stream.of(value.split(","))
				.forEach(item -> containsAllItemsInListPredicate.and(path.containsIgnoreCase(item)));
			
			return containsAllItemsInListPredicate;
		});
		
		bindings.bind(root.serveCount).all((MultiValueBinding<NumberPath<Integer>, Integer>) (path, values) -> {
			List<Integer> serveCountValues = new ArrayList<Integer>(values);
			if(values.size() == 1) {
				return Optional.of(path.eq(serveCountValues.get(0)));
			} else {
				return Optional.of(path.between(serveCountValues.get(0), serveCountValues.get(1)));
			}
		});
	}
}
