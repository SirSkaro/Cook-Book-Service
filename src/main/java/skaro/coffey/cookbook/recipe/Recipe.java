package skaro.coffey.cookbook.recipe;

import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import skaro.coffey.cookbook.ingredient.Ingredient;
import skaro.coffey.cookbook.tag.Tag;

@Entity
@Table
public class Recipe {

	@Id
	@Column
	@NotEmpty
	private String id;
	@Column
	@NotEmpty
	private String label;
	@Column
	@NotEmpty
	private String shortDescription;
	@Column
	private String instructions;
	@Column
	private String source;
	@Column(name="serve_count")
	@Positive
	private Integer serveCount;
	@OneToMany(
		mappedBy = Ingredient.RECIPE_MAPPED_BY_VALUE,
		orphanRemoval = true
	)
	private Set<Ingredient> ingredients;
	@ManyToMany
	@JoinTable(
		name = "recipe_tag",
		joinColumns = @JoinColumn(name = "recipe_id"),
		inverseJoinColumns = @JoinColumn(name = "tag_id")
	)
	Set<Tag> tags;
	
	public Recipe() {
		this.id = UUID.randomUUID().toString();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public String getInstructions() {
		return instructions;
	}
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Integer getServeCount() {
		return serveCount;
	}
	public void setServeCount(Integer serveCount) {
		this.serveCount = serveCount;
	}
	public Set<Ingredient> getIngredients() {
		return ingredients;
	}
	public void setIngredients(Set<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}
	public Set<Tag> getTags() {
		return tags;
	}
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
	
}
