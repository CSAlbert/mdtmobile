package com.mdt.crhf.mobile.controller;

import com.alibaba.fastjson.JSONObject;
import com.mdt.crhf.mobile.config.RestStatusCode;
import com.mdt.crhf.mobile.dao.CIEDRankingDao;
import com.mdt.crhf.mobile.entity.BackendData;
import com.mdt.crhf.mobile.exception.ParameterException;
import com.mdt.crhf.mobile.service.ICIEDRankingService;
import com.mdt.crhf.mobile.exception.SapBoException;
import com.mdt.crhf.mobile.service.ILogonTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    private ICIEDRankingService ICIEDRankingService;

    @Autowired
    private ILogonTokenService ILogonTokenService;


    /**
     * 接收app登录用户的name,和取得Bo的logontoken，存到session中
     *
     * @param request
     */
    @PostMapping(value = "/InitializeBo")
    public void initializeBo(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();

        // 定义appUserName、logonToken
        String appUserName = null;
        String logonToken = null;

        // 对appUserName、logonToken赋值
        appUserName = request.getParameter("name");
        logonToken = ILogonTokenService.getLogonToken();

        // 将appUserName、logonToken放到session中
        session.setAttribute("appUserName", appUserName);
        session.setAttribute("logonToken", logonToken);

        log.info("initializeBo执行成功！");
    }

    /**
     * CIEDRanking-Region
     *
     * @return
     */

    @PostMapping(value = "/Region")
    public JSONObject getRegionData(HttpServletRequest request) {
        log.debug("开始执行CIEDRankingController里面的getRegionData方法。");

        // 定义appUserName、logonToken
        String appUserName = null;
        String logonToken = null;

        // 通过session获取appUserName、logonToken的值
        appUserName = request.getSession().getAttribute("appUserName").toString();
        logonToken = request.getSession().getAttribute("logonToken").toString();

        BackendData backendData = new BackendData();
        JSONObject CIEDRankingRegionData = new JSONObject();

        try {
            // 取得region的所有数据
            CIEDRankingRegionData = ICIEDRankingService.getCIEDRankingData(appUserName, logonToken, CUID_Region,
                    reportName_Region);
            log.info("region请求状态码：" + RestStatusCode.SUCCESS);

            // 将请求状态、提示信息、数据存到实体类
            backendData.setCode(RestStatusCode.SUCCESS);
            backendData.setMessage("this is a logontoken");
            backendData.setData(CIEDRankingRegionData);
        } catch (Exception e) {
            log.info(e.getMessage());
            backendData.setCode(RestStatusCode.ERROR);
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
    @PostMapping(value = "/District")
    public JSONObject getDistrictData(HttpServletRequest request) {
        log.debug("开始执行CIEDRankingController里面的getDistrictData方法。");

        // 定义appUserName、logonToken
        String appUserName = null;
        String logonToken = null;

        // 通过session获取appUserName、logonToken的值
        appUserName = request.getSession().getAttribute("appUserName").toString();
        logonToken = request.getSession().getAttribute("logonToken").toString();

        BackendData backendData = new BackendData();
        JSONObject CIEDRankingDistrictData = new JSONObject();

        try {
            // 取得region的所有数据
            CIEDRankingDistrictData = ICIEDRankingService.getCIEDRankingData(appUserName, logonToken, CUID_District,
                    reportName_District);
            log.debug("district请求状态码：" + RestStatusCode.SUCCESS);

            // 将请求状态、提示信息、数据存到实体类
            backendData.setCode(RestStatusCode.SUCCESS);
            backendData.setMessage("this is a logontoken");
            backendData.setData(CIEDRankingDistrictData);
        }  catch (Exception e) {
            log.info(e.getMessage());
            backendData.setCode(RestStatusCode.ERROR);
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
    public JSONObject getTSRData(HttpServletRequest request) {
        log.debug("开始执行CIEDRankingController里面的getTSRData方法。");

        // 定义appUserName、logonToken
        String appUserName = null;
        String logonToken = null;

        // 通过session获取appUserName、logonToken的值
        appUserName = request.getSession().getAttribute("appUserName").toString();
        logonToken = request.getSession().getAttribute("logonToken").toString();

        BackendData backendData = new BackendData();
        JSONObject CIEDRankingTSRData = new JSONObject();

        try {
            // 取得region的所有数据
            CIEDRankingTSRData = ICIEDRankingService.getCIEDRankingData(appUserName, logonToken, CUID_TSR,
                    reportName_TSR);
            log.debug("region请求状态码：" + RestStatusCode.SUCCESS);

            // 将请求状态、提示信息、数据存到实体类
            backendData.setCode(RestStatusCode.SUCCESS);
            backendData.setMessage("this is a logontoken");
            backendData.setData(CIEDRankingTSRData);
        }  catch (Exception e) {
            log.info(e.getMessage());
            backendData.setCode(RestStatusCode.ERROR);
            backendData.setMessage(e.getMessage());
        }
//        ResponseEntity<BackendData> responseEntity = new ResponseEntity<BackendData>(backendData, HttpStatus.OK);
        log.debug("CIEDRankingController里面的getTSRData方法请求数据成功。TSR数据：" + CIEDRankingTSRData);
        return CIEDRankingTSRData;
    }

}
