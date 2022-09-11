package api.client;

public class IngredientsModel {
    private String[] ingredients;

    public IngredientsModel(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public IngredientsModel() {
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }
}
