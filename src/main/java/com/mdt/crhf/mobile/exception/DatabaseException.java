package com.mdt.crhf.mobile.exception;

/**
 * @author chulang
 * @version 1.0
 * @date 2019/11/2 11:47
 * @Description 数据库异常类
 */
public class DatabaseException extends Exception{
    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
