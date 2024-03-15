package diplom.client;

import diplom.dto.LogoutRequest;
import diplom.dto.UserCreateRequest;
import io.restassured.response.Response;

public class UserClient extends RestClient{

    public Response create(UserCreateRequest userCreateRequest) {
        return getDefaultRequestSpecification()
                .body(userCreateRequest)
                .when()
                .post("auth/register");
    }

    public Response delete(String accessToken) {
        return getDefaultRequestSpecification()
                .header("Authorization", accessToken)
                .when()
                .delete("auth/user");
    }

    public Response login(UserCreateRequest userCreateRequest) {
        return getDefaultRequestSpecification()
                .body(userCreateRequest)
                .when()
                .post("auth/login");
    }

    public Response updateUserInfo(UserCreateRequest userCreateRequest, String accessToken) {
        return getDefaultRequestSpecification()
                .header("Authorization", accessToken)
                .body(userCreateRequest)
                .when()
                .patch("auth/user");
    }

    public Response logout(LogoutRequest logoutRequest, String accessToken) {
        return getDefaultRequestSpecification()
                .header("Authorization", accessToken)
                .body(logoutRequest)
                .when()
                .post("auth/logout");
    }
}
