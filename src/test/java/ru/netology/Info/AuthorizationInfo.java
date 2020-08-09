package ru.netology.Info;

public class AuthorizationInfo {
    public final String login;
    public final String password;
    public final String status;

    private AuthorizationInfo(String login, String password, String status) {
        this.login = login;
        this.password = password;
        this.status = status;
    }

    public static AuthorizationInfo getActiveAuthorizationInfoByUserInfo(UserInfo userInfo) {
        return new AuthorizationInfo(
                userInfo.login,
                userInfo.password,
                "active"
        );
    }

    public static AuthorizationInfo getBlockedAuthorizationInfoByUserInfo(UserInfo userInfo) {
        return new AuthorizationInfo(
                userInfo.login,
                userInfo.password,
                "blocked"
        );
    }
}
