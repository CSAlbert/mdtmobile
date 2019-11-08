package com.mdt.crhf.mobile.serviceImpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mdt.crhf.mobile.dao.CIEDRankingDao;
import com.mdt.crhf.mobile.exception.ParameterException;
import com.mdt.crhf.mobile.exception.SapBoException;
import com.mdt.crhf.mobile.service.ICIEDRankingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author chulang
 * @version 1.0
 * @date 2019/11/2 12:56
 * @Description CIEDRankingService的实现类
 */

@Service
public class CIEDRankingServiceImpl implements ICIEDRankingService {

    private final Logger log = LoggerFactory.getLogger(CIEDRankingServiceImpl.class);

    @Autowired
    CIEDRankingDao ciedRankingDao;

    @Value("${webi.block.name.main.data}")
    private String mainDataBlockName;
    @Value("${webi.block.name.formatted.date}")
    private String formattedDateBlockName;

    /**
     * 通过cuid、pageName和appUserName，取到前端所需的数据（mainData、formattedDate、description）
     *
     * @param appUserName
     * @param cuid
     * @param reportName
     * @return
     * @throws ParameterException
     * @throws SapBoException
     */
    @Override
    public JSONObject getCIEDRankingData(String appUserName,String logonToken, String cuid, String reportName) throws ParameterException,
            SapBoException {

        // 检查参数合法性
        if (appUserName == "" || cuid == null || reportName == null) {
            throw new ParameterException("在CIEDRankingService类中调用getCIEDRankingData方法参数错误。userName = " + appUserName +
                    ",cuid = " + cuid + ",reportName = " + reportName);
        }

        JSONObject document = new JSONObject(); // document的属性信息
        String documentId = null; // 根据CUID取得的documentId
        String description = null; // document的描述信息
        JSONArray reports = new JSONArray(); // report列表
        String reportId = null; // 所需reportId
        String boReportContent = null; // 从BO获取的一个report的所有信息
        JSONObject dataBlockId = new JSONObject(); // 获取一个report的所需block信息
        String mainDataBlockId = null; // 主体数据的blockId
        String formattedDateBlockId = null; // 日期数据的blockId
        String formattedDate = null; // 日期数据内容
        JSONArray mainData = null; // 主数据
        JSONObject displayData = new JSONObject(); // 解析后的可供前端展示的所有数据组成的json数组格式

//        // 取得logonToken
//        logonToken = ciedRankingDao.getLogonToken();

        // 取document信息（documentId和description）
        document = ciedRankingDao.getDocumentIdAndDescription(logonToken, cuid);
        documentId = document.getString("documentId");
        description = document.getString("description");

        // 取数据前需要刷新报表
        ciedRankingDao.refreshDocumentWithParamenters(logonToken, documentId, appUserName);

        // 获取report信息
        reports = ciedRankingDao.getReportId(logonToken, documentId);
        for (int i = 0; i < reports.size(); i++) {
            if (reports.getJSONObject(i).getString("name").equals(reportName)) {
                reportId = reports.getJSONObject(i).getString("id");
                break;
            }
        }

        // 获取boReportComtent数据
        boReportContent = ciedRankingDao.getBoReportContent(logonToken,documentId,reportId);

        // 获取数据块名称对应的id
        dataBlockId = ciedRankingDao.getBoDataBlockId(logonToken,documentId,reportId);
        mainDataBlockId = dataBlockId.getString(mainDataBlockName);
        formattedDateBlockId = dataBlockId.getString(formattedDateBlockName);

        // 取得formattedDate、mainData数据
        formattedDate = ciedRankingDao.getFormattedDateOfBoContent(boReportContent,formattedDateBlockId);
        mainData = ciedRankingDao.getMainDataOfBoContent(boReportContent,mainDataBlockId);


        // 将所需数据（formattedDate、mainData、description）放到一个jsonObjece中
        displayData.put("formattedDate",formattedDate);
        displayData.put("description",description);
        displayData.put("mainData",mainData);

        log.debug("从BO系统完成所有取数操作，数据："+displayData);
        return displayData;
    }
}