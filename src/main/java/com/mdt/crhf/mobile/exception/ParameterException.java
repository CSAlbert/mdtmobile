package com.mdt.crhf.mobile.exception;

/**
 * @author chulang
 * @version 1.0
 * @date 2019/11/2 11:49
 * @Description 参数异常类
 */
public class ParameterException extends Exception {
    public ParameterException(String message) {
        super(message);
    }

    public ParameterException(String message,Throwable cause){
        super(message,cause);
    }
}
