package com.mdt.crhf.mobile.exception;

/**
 * @author chulang
 * @version 1.0
 * @date 2019/11/2 11:49
 * @Description 文件异常类
 */
public class FileException extends Exception {
    public FileException(String message) {
        super(message);
    }

    public FileException(String message,Throwable cause){
        super(message,cause);
    }
}
