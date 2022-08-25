package praktikum;

public class LoginUserResponseModel {
    private Boolean success;
    private UserLoginModel user;
    private String accessToken;
    private String refreshToken;

    public LoginUserResponseModel(Boolean success, UserLoginModel user, String accessToken, String refreshToken) {
        this.success = success;
        this.user = user;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public LoginUserResponseModel() {
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public UserLoginModel getUser() {
        return user;
    }

    public void setUser(UserLoginModel user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
