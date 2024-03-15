package diplom.client;

import diplom.dto.MakeOrderRequest;
import io.restassured.response.Response;

public class OrderClient extends RestClient{

    public Response getIngredients() {
        return getDefaultRequestSpecification()
                .when()
                .get("ingredients");
    }
    public Response make(MakeOrderRequest makeOrderRequest) {
        return getDefaultRequestSpecification()
                .body(makeOrderRequest)
                .when()
                .post("orders");
    }

    public Response getAllOrders() {
        return getDefaultRequestSpecification()
                .when()
                .get("orders/all");
    }

    public Response makeWithToken(MakeOrderRequest makeOrderRequest, String accessToken) {
        return getDefaultRequestSpecification()
                .header("Authorization", accessToken)
                .body(makeOrderRequest)
                .when()
                .post("orders");
    }

    public Response getAllOrdersWithToken(String accessToken) {
        return getDefaultRequestSpecification()
                .header("Authorization", accessToken)
                .when()
                .get("orders");
    }

    public Response getAllOrdersWithoutToken() {
        return getDefaultRequestSpecification()
                .when()
                .get("orders");
    }
}
