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
	private Double quantity;
	@Column
	private String units;
	@Column
	@NotNull
	private Boolean optional;
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
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
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
	public Recipe getRecipe() {
		return recipe;
	}
	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}
	
	
}
