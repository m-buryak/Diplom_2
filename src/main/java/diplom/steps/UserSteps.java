package diplom.steps;

import diplom.client.UserClient;
import diplom.dto.LogoutRequest;
import diplom.dto.UserCreateRequest;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class UserSteps {
    private final UserClient userClient;

    public UserSteps(UserClient userClient) {
        this.userClient = userClient;
    }

    @Step
    public ValidatableResponse create(String email, String name, String password) {
        UserCreateRequest requestBody = new UserCreateRequest();
        requestBody.setEmail(email);
        requestBody.setName(name);
        requestBody.setPassword(password);
        return  userClient.create(requestBody)
                .then();
    }

    @Step
    public ValidatableResponse delete(String accessToken) {
        return userClient.delete(accessToken)
                .then();
    }

    @Step
    public ValidatableResponse login(String email, String password) {
        UserCreateRequest requestBody = new UserCreateRequest();
        requestBody.setEmail(email);
        requestBody.setPassword(password);
        return userClient.login(requestBody)
                .then();
    }

    @Step
    public ValidatableResponse updateUserInfo(String email, String name, String password, String accessToken) {
        UserCreateRequest requestBody = new UserCreateRequest();
        requestBody.setEmail(email);
        requestBody.setName(name);
        requestBody.setPassword(password);
        return userClient.updateUserInfo(requestBody, accessToken)
                .then();
    }

    @Step
    public ValidatableResponse logout(String accessToken, String refreshToken) {
        LogoutRequest requestBody = new LogoutRequest();
        requestBody.setToken(refreshToken);
        return userClient.logout(requestBody, accessToken)
                .then();
    }
}
