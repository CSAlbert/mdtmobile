package com.mdt.crhf.mobile.controller;

import com.alibaba.fastjson.JSONObject;
import com.mdt.crhf.mobile.config.RestStatusCode;
import com.mdt.crhf.mobile.entity.BackendData;
import com.mdt.crhf.mobile.exception.ParameterException;
import com.mdt.crhf.mobile.service.CIEDRankingService;
import com.mdt.crhf.mobile.exception.SapBoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chulang
 * @version 1.0
 * @date 2019/11/2 12:42
 * @Description CIEDRanking的Controller层
 */

@RestController
@RequestMapping("/CIEDRanking")
public class CIEDRankingController {

    private final Logger log = LoggerFactory.getLogger(CIEDRankingController.class);

    // 存放从app取得的用户名
    String appUserName = null;

    // 从配置文件取报表的cuid和reportName
    @Value("${webi.cied.ranking.region.cuid}")
    private String CUID_Region;
    @Value("${webi.cied.ranking.region.report.name}")
    private String reportName_Region;

    @Value("${webi.cied.ranking.district.cuid}")
    private String CUID_District;
    @Value("${webi.cied.ranking.district.report.name}")
    private String reportName_District;

    @Value("${webi.cied.ranking.tsr.cuid}")
    private String CUID_TSR;
    @Value("${webi.cied.ranking.tsr.report.name}")
    private String reportName_TSR;

    @Autowired
    private CIEDRankingService ciedRankingService;

    /**
     * 接收app登录用户的name
     *
     * @param request
     */
    @RequestMapping(value = "/appUserName", method = RequestMethod.POST)
    public void saveAppUserName(HttpServletRequest request) {
        log.debug("开始执行saveAppUserName");
        appUserName = request.getParameter("name");
        // TODO:根据jssdk获取的用户名进行相应改变
        log.debug("成功接收appUserName：" + appUserName);

    }

    /**
     * CIEDRanking-Region
     *
     * @return
     */
    @RequestMapping(value = "/Region", method = RequestMethod.POST)
    public JSONObject getRegionData() {
        log.debug("开始执行CIEDRankingController里面的getRegionData方法。");

        BackendData backendData = new BackendData();
        JSONObject CIEDRankingRegionData = new JSONObject();

        try {
            // 取得region的所有数据
            CIEDRankingRegionData = ciedRankingService.getCIEDRankingData(appUserName, CUID_Region, reportName_Region);
            log.debug("region请求状态码：" + RestStatusCode.SUCCESS);

            // 将请求状态、提示信息、数据存到实体类
            backendData.setCode(RestStatusCode.SUCCESS);
            backendData.setMessage("this is a logontoken");
            backendData.setData(CIEDRankingRegionData);
        } catch (ParameterException e) {
            log.info(e.getMessage());
            backendData.setCode(RestStatusCode.ERROR_PARAMETER);
            backendData.setMessage(e.getMessage());
        } catch (SapBoException e) {
            log.info(e.getMessage());
            backendData.setCode(RestStatusCode.ERROR_SAPBO);
            backendData.setMessage(e.getMessage());
        }
//        ResponseEntity<BackendData> responseEntity = new ResponseEntity<BackendData>(backendData, HttpStatus.OK);
        log.debug("CIEDRankingController里面的getRegionData方法请求数据成功。region数据：" + CIEDRankingRegionData);
        return CIEDRankingRegionData;
    }

    /**
     * CIEDRanking-District
     *
     * @return
     */
    @RequestMapping(value = "/District", method = RequestMethod.POST)
    public JSONObject getDistrictData() {
        log.debug("开始执行CIEDRankingController里面的getDistrictData方法。");

        BackendData backendData = new BackendData();
        JSONObject CIEDRankingDistrictData = new JSONObject();

        try {
            // 取得region的所有数据
            CIEDRankingDistrictData = ciedRankingService.getCIEDRankingData(appUserName, CUID_District, reportName_District);
            log.debug("district请求状态码：" + RestStatusCode.SUCCESS);

            // 将请求状态、提示信息、数据存到实体类
            backendData.setCode(RestStatusCode.SUCCESS);
            backendData.setMessage("this is a logontoken");
            backendData.setData(CIEDRankingDistrictData);
        } catch (ParameterException e) {
            log.info(e.getMessage());
            backendData.setCode(RestStatusCode.ERROR_PARAMETER);
            backendData.setMessage(e.getMessage());
        } catch (SapBoException e) {
            log.info(e.getMessage());
            backendData.setCode(RestStatusCode.ERROR_SAPBO);
            backendData.setMessage(e.getMessage());
        }
//        ResponseEntity<BackendData> responseEntity = new ResponseEntity<BackendData>(backendData, HttpStatus.OK);
        log.debug("CIEDRankingController里面的getDistrictData方法请求数据成功。district数据：" + CIEDRankingDistrictData);
        return CIEDRankingDistrictData;
    }

    /**
     * CIEDRanking-TSR
     *
     * @return
     */
    @RequestMapping(value = "/TSR", method = RequestMethod.POST)
    public JSONObject getTSRData() {
        log.debug("开始执行CIEDRankingController里面的getTSRData方法。");

        BackendData backendData = new BackendData();
        JSONObject CIEDRankingTSRData = new JSONObject();

        try {
            // 取得region的所有数据
            CIEDRankingTSRData = ciedRankingService.getCIEDRankingData(appUserName, CUID_TSR, reportName_TSR);
            log.debug("region请求状态码：" + RestStatusCode.SUCCESS);

            // 将请求状态、提示信息、数据存到实体类
            backendData.setCode(RestStatusCode.SUCCESS);
            backendData.setMessage("this is a logontoken");
            backendData.setData(CIEDRankingTSRData);
        } catch (ParameterException e) {
            log.info(e.getMessage());
            backendData.setCode(RestStatusCode.ERROR_PARAMETER);
            backendData.setMessage(e.getMessage());
        } catch (SapBoException e) {
            log.info(e.getMessage());
            backendData.setCode(RestStatusCode.ERROR_SAPBO);
            backendData.setMessage(e.getMessage());
        }
//        ResponseEntity<BackendData> responseEntity = new ResponseEntity<BackendData>(backendData, HttpStatus.OK);
        log.debug("CIEDRankingController里面的getTSRData方法请求数据成功。TSR数据：" + CIEDRankingTSRData);
        return CIEDRankingTSRData;
    }

}
