package com.mdt.crhf.mobile.service;

import com.alibaba.fastjson.JSONObject;
import com.mdt.crhf.mobile.exception.ParameterException;
import com.mdt.crhf.mobile.exception.SapBoException;

/**
 * @author chulang
 * @version 1.0
 * @date 2019/11/2 12:53
 * @Description CIEDRanking的service层接口
 */
public interface ICIEDRankingService {
    JSONObject getCIEDRankingData(String appUserName,String logonToken, String cuid, String reportName) throws ParameterException,
            SapBoException;
}
