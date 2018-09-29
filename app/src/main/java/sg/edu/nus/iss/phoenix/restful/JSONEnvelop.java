package sg.edu.nus.iss.phoenix.restful;

import java.io.Serializable;

public class JSONEnvelop<T extends Object> implements Serializable {

    private T data;
    private Error error;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public JSONEnvelop() {
    }

    public JSONEnvelop(T data, Error error) {
        this.data = data;
        this.error = error;
    }

}
