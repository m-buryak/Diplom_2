package diplom.dto;

public class LogoutRequest {
    private String token;

    public LogoutRequest(String token) {
        this.token = token;
    }

    public LogoutRequest() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
