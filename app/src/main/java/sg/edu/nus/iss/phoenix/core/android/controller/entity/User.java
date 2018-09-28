package sg.edu.nus.iss.phoenix.core.android.controller.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private String id;
    private String userName;
    private String userPassword;
    private List<Role> roles;

    public User(String id,String userName, String userPassword, List<Role> roles) {
        this.id=id;
        this.userName = userName;
        this.userPassword= userPassword;
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
