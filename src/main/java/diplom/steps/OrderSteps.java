package diplom.steps;

import diplom.client.OrderClient;
import diplom.dto.MakeOrderRequest;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.List;

public class OrderSteps {
    private final OrderClient orderClient;

    public OrderSteps(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    @Step
    public ValidatableResponse getIngredients() {
        return orderClient.getIngredients()
                .then();
    }

    @Step
    public ValidatableResponse make(List<String> ingredients) {
        MakeOrderRequest requestBody = new MakeOrderRequest();
        requestBody.setIngredients(ingredients);
        return orderClient.make(requestBody)
                .then();
    }
    @Step
    public ValidatableResponse makeWithToken(List<String> ingredients, String accessToken) {
        MakeOrderRequest requestBody = new MakeOrderRequest();
        requestBody.setIngredients(ingredients);
        return orderClient.makeWithToken(requestBody, accessToken)
                .then();
    }

    @Step
    public ValidatableResponse getAllOrdersWithToken(String accessToken) {
        return orderClient.getAllOrdersWithToken(accessToken)
                .then();
    }

    @Step
    public ValidatableResponse getAllOrdersWithoutToken() {
        return orderClient.getAllOrdersWithoutToken()
                .then();
    }
}
