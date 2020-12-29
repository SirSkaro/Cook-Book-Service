package skaro.coffey.cookbook.ingredient;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import skaro.coffey.cookbook.recipe.Recipe;

@Entity
@Table
public class Ingredient {
	public static final String RECIPE_MAPPED_BY_VALUE = "recipe";
	
	@Id
	@Column
	@NotEmpty
	private String id;
	@Column
	@NotEmpty
	private String label;
	@Column
	private String quantityMin;
	@Column
	private String quantityMax;
	@Column
	private String units;
	@Column
	@NotNull
	private Boolean optional;
	@Column
	@Positive
	private Integer sortOrder;
	@ManyToOne
	@JoinColumn(name="recipe_id")
	private Recipe recipe;
	
	public Ingredient() {
		this.id = UUID.randomUUID().toString();
		this.optional = true;
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
	public String getQuantityMin() {
		return quantityMin;
	}
	public void setQuantityMin(String quantityMin) {
		this.quantityMin = quantityMin;
	}
	public String getQuantityMax() {
		return quantityMax;
	}
	public void setQuantityMax(String quantityMax) {
		this.quantityMax = quantityMax;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public Boolean getOptional() {
		return optional;
	}
	public void setOptional(Boolean optional) {
		this.optional = optional;
	}
	public Integer getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	public Recipe getRecipe() {
		return recipe;
	}
	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}
	
	
}
