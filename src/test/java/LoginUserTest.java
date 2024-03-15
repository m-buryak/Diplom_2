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

public class LoginUserTest {

    private static UserSteps userSteps;

    private static List<String> accessTokens = new ArrayList<>();

    @Before
    public void setUp() {
        userSteps = new UserSteps(new UserClient());
    }

    @Test
    public void loginNewUserTest() {
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
    }

    @Test
    public void loginWithIncorrectEmailTest() {
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

        String incorrectEmail = email + RandomStringUtils.random(1);

        userSteps.login(incorrectEmail, password)
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", Matchers.is("email or password are incorrect"))
                .body("success", Matchers.is(false));
    }

    @Test
    public void loginWithIncorrectPasswordTest() {
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

        String incorrectPassword = password + RandomStringUtils.random(1);

        userSteps.login(email, incorrectPassword)
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", Matchers.is("email or password are incorrect"))
                .body("success", Matchers.is(false));
    }

    @AfterClass
    public static void tearDown() {
        for (String accessToken : accessTokens) {
            userSteps.delete(accessToken);
        }
    }

}
