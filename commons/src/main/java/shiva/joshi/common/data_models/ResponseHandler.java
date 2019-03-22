package shiva.joshi.common.data_models;

/**
 * Author - J.K.Joshi
 * Date -  25-10-2016.
 */

public class ResponseHandler {

    private boolean mExecuted;
    private String mMessage;
    private Object value;

    public ResponseHandler() {
        this.mExecuted = false;
        this.mMessage = "Something went wrong";
        this.value = null;
    }
    public ResponseHandler(String message) {
        this.mExecuted = false;
        this.mMessage = message;
        this.value = null;
    }

    public boolean isExecuted() {
        return mExecuted;
    }

    public void setExecuted(boolean executed) {
        this.mExecuted = executed;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ResponseHandler{" +
                "mExecuted=" + mExecuted +
                ", mMessage='" + mMessage + '\'' +
                ", value=" + value +
                '}';
    }
}
