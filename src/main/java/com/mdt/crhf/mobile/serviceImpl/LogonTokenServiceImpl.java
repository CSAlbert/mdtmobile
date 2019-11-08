package com.mdt.crhf.mobile.serviceImpl;

import com.mdt.crhf.mobile.dao.CIEDRankingDao;
import com.mdt.crhf.mobile.exception.SapBoException;
import com.mdt.crhf.mobile.service.ILogonTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chulang
 * @version 1.0
 * @date 2019/11/8 15:54
 * @Description TODO
 */

@Service
public class LogonTokenServiceImpl implements ILogonTokenService {

    private final Logger log = LoggerFactory.getLogger(LogonTokenServiceImpl.class);

    @Autowired
    CIEDRankingDao ciedRankingDao;

    /**
     * 获取logonToken
     *
     * @return
     * @throws SapBoException
     */
    @Override
    public String getLogonToken() throws SapBoException {
        String logonToken = null;
        logonToken = ciedRankingDao.getLogonToken();

        if (logonToken == null){
            throw new SapBoException("获取LogonToken失败！");
        }
        log.info("成功取得logonToken！");
        return logonToken;
    }
}
