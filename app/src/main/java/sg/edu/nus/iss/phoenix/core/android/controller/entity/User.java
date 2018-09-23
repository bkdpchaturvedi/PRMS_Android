package sg.edu.nus.iss.phoenix.core.android.controller.entity;

public class User {

    private String userName;
    private String userPassword;
    private String userRoles;

    public User(String userName, String userPassword, String userRoles) {
        this.userName = userName;
        this.userPassword= userPassword;
        this.userRoles = userRoles;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserRoles() {
        return userRoles;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserRoles(String userRoles) {
        this.userRoles = userRoles;
    }
}
