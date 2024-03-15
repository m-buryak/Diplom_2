import diplom.client.OrderClient;
import diplom.client.UserClient;
import diplom.dto.GetIngredientsResponse;
import diplom.dto.Ingredient;
import diplom.steps.OrderSteps;
import diplom.steps.UserSteps;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MakeOrderTest {
    private static UserSteps userSteps;
    private static OrderSteps orderSteps;

    private static List<String> accessTokens = new ArrayList<>();

    @Before
    public void setUp() {
        userSteps = new UserSteps(new UserClient());
        orderSteps = new OrderSteps(new OrderClient());
    }

    @Test
    public void makeOrderWithoutAuthorizationWithIngredientsTest() {
        GetIngredientsResponse ingredients;
        ingredients = orderSteps.getIngredients().extract().as(GetIngredientsResponse.class);
        List<String> ingredientsHash = new ArrayList<>();
        for (Ingredient ingredient: ingredients.getData()) {
            ingredientsHash.add(ingredient.get_id());
        }

        int randomNum = ThreadLocalRandom.current().nextInt(1, ingredientsHash.size() + 1);

        orderSteps.make(ingredientsHash.subList(0, randomNum))
                .statusCode(HttpStatus.SC_OK)
                .body("success", Matchers.is(true));
    }

    @Test
    public void makeOrderWithAuthorizationTest() {
        String email = RandomStringUtils.randomAlphabetic(10).toLowerCase() + "@" + RandomStringUtils.randomAlphabetic(6).toLowerCase() + '.' + RandomStringUtils.randomAlphabetic(3).toLowerCase();
        String name = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);

        userSteps.create(email, name, password)
                .statusCode(HttpStatus.SC_OK)
                .body("success", Matchers.is(true))
                .body("user.email", Matchers.is(email))
                .body("user.name", Matchers.is(name));



        String accessToken = userSteps.login(email, password)
                .statusCode(HttpStatus.SC_OK)
                .body("user.email", Matchers.is(email))
                .body("user.name", Matchers.is(name))
                .extract().path("accessToken");

        accessTokens.add(accessToken);

        GetIngredientsResponse ingredients;
        ingredients = orderSteps.getIngredients().extract().as(GetIngredientsResponse.class);
        List<String> ingredientsHash = new ArrayList<>();
        for (Ingredient ingredient: ingredients.getData()) {
            ingredientsHash.add(ingredient.get_id());
        }

        int randomNum = ThreadLocalRandom.current().nextInt(1, ingredientsHash.size() + 1);

        orderSteps.makeWithToken(ingredientsHash.subList(0, randomNum), accessToken)
                .statusCode(HttpStatus.SC_OK)
                .body("success", Matchers.is(true));
    }

    @Test
    public void makeOrderWithoutAuthorizationWithoutIngredientsTest() {
        List<String> ingredientsHash = new ArrayList<>();

        orderSteps.make(ingredientsHash)
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("success", Matchers.is(false))
                .body("message", Matchers.is("Ingredient ids must be provided"));
    }

    @Test
    public void makeOrderWithoutAuthorizationWithWrongIngredientsTest() {
        List<String> ingredientsHash = new ArrayList<>();
        ingredientsHash.add("wrong_ingredient_1");
        ingredientsHash.add("wrong_ingredient_2");

        orderSteps.make(ingredientsHash)
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @AfterClass
    public static void tearDown() {
        for (String accessToken : accessTokens) {
            userSteps.delete(accessToken);
        }
    }
}
