package sg.edu.nus.iss.phoenix.core.android.controller.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private String id;
    private String userName;
    private String userPassword;
    private List<Role> userRoles;

    public User(String id,String userName, String userPassword, List<Role> userRoles) {
        this.id=id;
        this.userName = userName;
        this.userPassword= userPassword;
        this.userRoles = userRoles;
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

    public List<Role> getUserRoles() {
        return userRoles;
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

    public void setUserRoles(ArrayList<Role> userRoles) {
        this.userRoles = userRoles;
    }
}
