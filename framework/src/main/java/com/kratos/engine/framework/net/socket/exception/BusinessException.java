package com.kratos.engine.framework.net.socket.exception;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 6448777609998876576L;

    public BusinessException() {
    }

    public BusinessException(String message) {
    	super(message, null, false, false);    	  
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression,
                             boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
