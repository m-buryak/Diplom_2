import diplom.client.OrderClient;
import diplom.client.UserClient;
import diplom.dto.GetIngredientsResponse;
import diplom.dto.GetOrdersResponse;
import diplom.dto.Ingredient;
import diplom.steps.OrderSteps;
import diplom.steps.UserSteps;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GetOrdersTest {
    private static UserSteps userSteps;
    private static OrderSteps orderSteps;

    private static List<String> accessTokens = new ArrayList<>();

    @Before
    public void setUp() {
        userSteps = new UserSteps(new UserClient());
        orderSteps = new OrderSteps(new OrderClient());
    }

    @Test
    public void getOrdersWithoutAuthorization() {
        orderSteps.getAllOrdersWithoutToken()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", Matchers.is(false))
                .body("message", Matchers.is("You should be authorised"));
    }

    @Test
    public void getOrdersWithAuthorizationTest() {
        String email = RandomStringUtils.randomAlphabetic(10).toLowerCase() + "@" + RandomStringUtils.randomAlphabetic(6).toLowerCase() + '.' + RandomStringUtils.randomAlphabetic(3).toLowerCase();
        String name = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);

        String accessToken = userSteps.create(email, name, password)
                .statusCode(HttpStatus.SC_OK)
                .body("success", Matchers.is(true))
                .body("user.email", Matchers.is(email))
                .body("user.name", Matchers.is(name))
                .extract().path("accessToken");

        accessTokens.add(accessToken);

        userSteps.login(email, password)
                .statusCode(HttpStatus.SC_OK)
                .body("user.email", Matchers.is(email))
                .body("user.name", Matchers.is(name));

        GetIngredientsResponse ingredients;
        ingredients = orderSteps.getIngredients().extract().as(GetIngredientsResponse.class);
        List<String> ingredientsHash = new ArrayList<>();
        for (Ingredient ingredient: ingredients.getData()) {
            ingredientsHash.add(ingredient.get_id());
        }

        int randomNum = ThreadLocalRandom.current().nextInt(1, ingredientsHash.size() + 1);

        int number = orderSteps.makeWithToken(ingredientsHash.subList(0, randomNum), accessToken)
                .statusCode(HttpStatus.SC_OK)
                .body("success", Matchers.is(true))
                .extract().path("order.number");

        GetOrdersResponse orders = orderSteps.getAllOrdersWithToken(accessToken)
                .statusCode(HttpStatus.SC_OK)
                .body("success", Matchers.is(true))
                .extract().as(GetOrdersResponse.class);

        Assert.assertEquals(number, (int)orders.getOrders().get(orders.getOrders().size() - 1).getNumber());

        orderSteps.getAllOrdersWithToken(accessToken)
                .statusCode(HttpStatus.SC_OK)
                .body("success", Matchers.is(true))
                .body("total", Matchers.is(orders.getOrders().size()));
    }
    @AfterClass
    public static void tearDown() {
        for (String accessToken : accessTokens) {
            userSteps.delete(accessToken);
        }
    }
}
