package com.template.exception;

/**
 * packageName    : com.template.exception
 * fileName       : TemplateException
 * author         : Jihun Park
 * date           : 2022/03/18
 * description    : Template Exception handler
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/03/18        Jihun Park       최초 생성
 */
public class TemplateException extends RuntimeException {

    public TemplateException() {
        super();
    }

    public TemplateException(String message) {
        super(message);
    }

    public TemplateException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateException(Throwable cause) {
        super(cause);
    }

    protected TemplateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
