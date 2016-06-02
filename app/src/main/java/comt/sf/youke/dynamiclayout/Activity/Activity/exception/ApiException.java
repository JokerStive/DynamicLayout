package comt.sf.youke.dynamiclayout.Activity.Activity.exception;

/**
 * Created by youke on 2016/5/25.
 * 请求fail抛出的异常
 */
public class ApiException extends Throwable {
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
