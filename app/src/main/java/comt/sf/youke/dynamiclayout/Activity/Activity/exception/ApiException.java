package comt.sf.youke.dynamiclayout.Activity.Activity.exception;

/**
 * Created by Administrator on 2016/5/25.
 */
public class ApiException extends Exception {
    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public ApiException(String message,int code) {
        this.code = code;
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
