package com.mdt.crhf.mobile.service;

import com.mdt.crhf.mobile.exception.SapBoException;

/**
 * @author chulang
 * @version 1.0
 * @date 2019/11/8 15:52
 * @Description TODO
 */
public interface ILogonTokenService {
    public String getLogonToken() throws SapBoException;
}
