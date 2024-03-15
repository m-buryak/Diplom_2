import diplom.client.UserClient;
import diplom.steps.UserSteps;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CreateUserTest {

    private static UserSteps userSteps;

    private static List<String> accessTokens = new ArrayList<>();

    @Before
    public void setUp() {
        userSteps = new UserSteps(new UserClient());
    }

    @Test
    public void successfulCreateUserTest() {
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
    }

    @Test
    public void createAlreadyExistUserTest() {
        String email = RandomStringUtils.randomAlphabetic(10).toLowerCase() + "@" + RandomStringUtils.randomAlphabetic(6).toLowerCase() + '.' + RandomStringUtils.randomAlphabetic(3).toLowerCase();
        String name = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);

        String accessToken = userSteps.create(email, name, password)
                .statusCode(HttpStatus.SC_OK)
                .body("success", Matchers.is(true))
                .body("user.email", Matchers.is(email))
                .body("user.name", Matchers.is(name))
                .extract().path("accessToken");

        userSteps.create(email, name, password)
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", Matchers.is(false))
                .body("message", Matchers.is("User already exists"));

        accessTokens.add(accessToken);
    }

    @Test
    public void createUserWithoutEmailTest() {
        String email = null;
        String name = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);

        userSteps.create(email, name, password)
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", Matchers.is(false))
                .body("message", Matchers.is("Email, password and name are required fields"));

    }

    @Test
    public void createUserWithoutNameTest() {
        String email = RandomStringUtils.random(10) + "@" + RandomStringUtils.random(6) + '.' + RandomStringUtils.random(3);
        String name = null;
        String password = RandomStringUtils.random(10);

        userSteps.create(email, name, password)
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", Matchers.is(false))
                .body("message", Matchers.is("Email, password and name are required fields"));

    }

    @Test
    public void createUserWithoutPasswordTest() {
        String email = RandomStringUtils.random(10) + "@" + RandomStringUtils.random(6) + '.' + RandomStringUtils.random(3);
        String name = RandomStringUtils.random(10);
        String password = null;

        userSteps.create(email, name, password)
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", Matchers.is(false))
                .body("message", Matchers.is("Email, password and name are required fields"));

    }

    @AfterClass
    public static void tearDown() {
        for (String accessToken : accessTokens) {
            userSteps.delete(accessToken);
        }
    }

}
