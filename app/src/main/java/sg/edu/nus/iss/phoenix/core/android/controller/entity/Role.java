package sg.edu.nus.iss.phoenix.core.android.controller.entity;

public class Role {

    private String role;
    private String accessPrivilege;

    public Role(String role,String accessPrivilege) {
        this.role=role;
        this.accessPrivilege = accessPrivilege;
    }

    public String getRole() {
        return role;
    }

    public String getAccessPrivilege() {
        return accessPrivilege;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setAccessPrivilege(String accessPrivilege) {
        this.accessPrivilege = accessPrivilege;
    }
}
