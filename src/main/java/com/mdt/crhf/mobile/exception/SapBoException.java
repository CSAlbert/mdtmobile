package com.mdt.crhf.mobile.exception;

/**
 * @author chulang
 * @version 1.0
 * @date 2019/11/2 11:50
 * @Description SAP相关异常类
 */
public class SapBoException extends Exception {
    public SapBoException(String message) {
        super(message);
    }

    public SapBoException(String message,Throwable cause){
        super(message,cause);
    }
}
