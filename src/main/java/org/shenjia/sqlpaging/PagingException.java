package org.shenjia.sqlpaging;

public class PagingException extends RuntimeException {

    private static final long serialVersionUID = -387511186215032760L;

    public PagingException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
