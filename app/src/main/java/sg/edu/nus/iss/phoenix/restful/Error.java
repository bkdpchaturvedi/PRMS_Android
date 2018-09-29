package sg.edu.nus.iss.phoenix.restful;

import java.io.Serializable;

public class Error implements Serializable {

    private String description;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Error(String error, String description) {
        this.error = error;
        this.description = description;
    }

}
