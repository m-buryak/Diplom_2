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

public class UpdateUserInfoTest {

    private static UserSteps userSteps;

    private static List<String> accessTokens = new ArrayList<>();

    @Before
    public void setUp() {
        userSteps = new UserSteps(new UserClient());
    }

    @Test
    public void updateUserEmailWithAuthorizationTest() {
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
                .body("success", Matchers.is(true))
                .body("user.email", Matchers.is(email))
                .body("user.name", Matchers.is(name))
                .extract().path("accessToken");

        String newEmail = email + RandomStringUtils.randomAlphabetic(1).toLowerCase();

        userSteps.updateUserInfo(newEmail, null, null, accessToken)
                .statusCode(HttpStatus.SC_OK)
                .body("success", Matchers.is(true))
                .body("user.email", Matchers.is(newEmail))
                .body("user.name", Matchers.is(name));

        accessTokens.add(accessToken);
    }

    @Test
    public void updateUserNameWithAuthorizationTest() {
        String email = RandomStringUtils.randomAlphabetic(10).toLowerCase() + "@" + RandomStringUtils.random(6).toLowerCase() + '.' + RandomStringUtils.random(3).toLowerCase();
        String name = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);

        userSteps.create(email, name, password)
                .statusCode(HttpStatus.SC_OK)
                .body("success", Matchers.is(true))
                .body("user.email", Matchers.is(email))
                .body("user.name", Matchers.is(name));


        String accessToken = userSteps.login(email, password)
                .statusCode(HttpStatus.SC_OK)
                .body("success", Matchers.is(true))
                .body("user.email", Matchers.is(email))
                .body("user.name", Matchers.is(name))
                .extract().path("accessToken");

        accessTokens.add(accessToken);

        String newName = name + RandomStringUtils.randomAlphabetic(1);

        userSteps.updateUserInfo(null, newName, null, accessToken)
                .statusCode(HttpStatus.SC_OK)
                .body("success", Matchers.is(true))
                .body("user.email", Matchers.is(email))
                .body("user.name", Matchers.is(newName));

        accessTokens.add(accessToken);
    }

    @Test
    public void updateUserPasswordWithAuthorizationTest() {
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
                .body("success", Matchers.is(true))
                .body("user.email", Matchers.is(email))
                .body("user.name", Matchers.is(name))
                .extract().path("accessToken");

        String newPassword = password + RandomStringUtils.randomAlphabetic(1);

        userSteps.updateUserInfo(null, null, newPassword, accessToken)
                .statusCode(HttpStatus.SC_OK)
                .body("success", Matchers.is(true))
                .body("user.email", Matchers.is(email))
                .body("user.name", Matchers.is(name));

        userSteps.login(email, password)
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", Matchers.is(false))
                .body("message", Matchers.is("email or password are incorrect"));

        accessToken = userSteps.login(email, newPassword)
                .statusCode(HttpStatus.SC_OK)
                .body("success", Matchers.is(true))
                .body("user.email", Matchers.is(email))
                .body("user.name", Matchers.is(name))
                .extract().path("accessToken");

        accessTokens.add(accessToken);
    }

    @Test
    public void updateUserEmailWithExistedEmailTest() {
        String email = RandomStringUtils.randomAlphabetic(10).toLowerCase() + "@" + RandomStringUtils.randomAlphabetic(6).toLowerCase() + '.' + RandomStringUtils.randomAlphabetic(3).toLowerCase();
        String name = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);

        userSteps.create(email, name, password)
                .statusCode(HttpStatus.SC_OK)
                .body("success", Matchers.is(true))
                .body("user.email", Matchers.is(email))
                .body("user.name", Matchers.is(name));



        String anotherEmail = RandomStringUtils.randomAlphabetic(10).toLowerCase() + "@" + RandomStringUtils.randomAlphabetic(6).toLowerCase() + '.' + RandomStringUtils.randomAlphabetic(3).toLowerCase();
        String anotherName = RandomStringUtils.randomAlphabetic(10);
        String anotherPassword = RandomStringUtils.randomAlphabetic(10);

        String anotherAccessToken = userSteps.create(anotherEmail, anotherName, anotherPassword)
                .statusCode(HttpStatus.SC_OK)
                .body("success", Matchers.is(true))
                .body("user.email", Matchers.is(anotherEmail))
                .body("user.name", Matchers.is(anotherName))
                .extract().path("accessToken");

        accessTokens.add(anotherAccessToken);

        String accessToken = userSteps.login(email, password)
                .statusCode(HttpStatus.SC_OK)
                .body("success", Matchers.is(true))
                .body("user.email", Matchers.is(email))
                .body("user.name", Matchers.is(name))
                .extract().path("accessToken");

        accessTokens.add(accessToken);

        userSteps.updateUserInfo(anotherEmail, null, null, accessToken)
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", Matchers.is(false))
                .body("message", Matchers.is("User with such email already exists"));
    }

    @Test
    public void updateUserEmailWithoutAuthorizationTest() {
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

        String refreshToken = userSteps.login(email, password)
                .statusCode(HttpStatus.SC_OK)
                .body("success", Matchers.is(true))
                .body("user.email", Matchers.is(email))
                .body("user.name", Matchers.is(name))
                .extract().path("refreshToken");

        userSteps.logout(accessToken, refreshToken)
                .statusCode(HttpStatus.SC_OK)
                .body("success", Matchers.is(true))
                .body("message", Matchers.is("Successful logout"));

        String newName = name + RandomStringUtils.randomAlphabetic(1);
        String newEmail = email + RandomStringUtils.randomAlphabetic(1).toLowerCase();

        userSteps.updateUserInfo(newEmail, newName, null, accessToken)
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", Matchers.is(false))
                .body("message", Matchers.is("You should be authorised"));
    }

    @Test
    public void updateUserEmailWithoutAccessToken() {
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

        String newName = name + RandomStringUtils.randomAlphabetic(1);
        String newEmail = email + RandomStringUtils.randomAlphabetic(1).toLowerCase();

        userSteps.updateUserInfo(newEmail, newName, null, "")
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", Matchers.is(false))
                .body("message", Matchers.is("You should be authorised"));
    }

    @AfterClass
    public static void tearDown() {
        for (String accessToken : accessTokens) {
            userSteps.delete(accessToken);
        }
    }

}
