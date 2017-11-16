package com.changhong.db.mongo;

/**
* Created by lanjian
*/
public class QueryException extends RuntimeException {
    public QueryException() {
        super();
    }

    public QueryException(String message) {
        super(message);
    }

    public QueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public QueryException(Throwable cause) {
        super(cause);
    }
}
