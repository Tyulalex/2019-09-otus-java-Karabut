package webserver.com.servlet.exceptions;

import com.google.gson.annotations.Expose;
import lombok.Getter;

import javax.servlet.http.HttpServletResponse;

@Getter
public class ErrorHttpException extends RuntimeException {

    @Expose
    private String message;

    @Expose
    private int statusCode = 1;

    private int httpStatusCode;

    public ErrorHttpException(Exception e, int httpStatusCode) {
        super(e);
        this.message = e.getMessage();
        this.httpStatusCode = httpStatusCode;
    }

    public ErrorHttpException(Exception e) {
        this(e, HttpServletResponse.SC_BAD_REQUEST);
    }

    public ErrorHttpException(String message) {
        this(message, HttpServletResponse.SC_BAD_REQUEST);

    }

    public ErrorHttpException(String message, int httpStatusCode) {
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
