package com.mdt.crhf.mobile.exception;

/**
 * @author chulang
 * @version 1.0
 * @date 2019/11/2 11:50
 * @Description 业务逻辑异常类
 */
public class LogicException extends Exception {
    public LogicException(String message) {
        super(message);
    }

    public LogicException(String message,Throwable cause){
        super(message,cause);
    }
}
