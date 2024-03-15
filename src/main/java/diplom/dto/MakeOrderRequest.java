package diplom.dto;

import java.util.List;

public class MakeOrderRequest {
    List<String> ingredients;

    public MakeOrderRequest(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public MakeOrderRequest() {
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
