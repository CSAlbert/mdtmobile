package com.mdt.crhf.mobile.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mdt.crhf.mobile.entity.User;
import com.mdt.crhf.mobile.exception.ParameterException;
import com.mdt.crhf.mobile.exception.SapBoException;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;

/**
 * @author chulang
 * @version 1.0
 * @date 2019/11/2 10:27
 * @Description CIEDRanking的Dao层
 */

@Repository
public class CIEDRankingDao {

    private final Logger log = LoggerFactory.getLogger(CIEDRankingDao.class);

    @Value("${bo.url}")
    private String boUrl;
    @Value("${bo.username}")
    private String boUserName;
    @Value("${bo.password}")
    private String boPassword;
    @Value("${bo.auth}")
    private String boAuth;
    @Value("${webi.block.name.formatted.date}")
    private String formattedDateBlockName;
    @Value("${webi.block.name.main.data}")
    private String mainDataBlockName;

    /**
     * 通过用户信息取得logonToken
     *
     * @return
     * @throws SapBoException
     */
    public String getLogonToken() throws SapBoException {
        log.debug("开始执行CIEDRankingDao的getLogonToken方法");

        String logonToken = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 创建Post请求
        HttpPost httpPost = new HttpPost(boUrl + "/biprws/logon/long");

        // 创建User对象
        User user = new User();
        user.setPassword(boPassword);
        user.setClientType("");
        user.setAuth(boAuth);
        user.setUserName(boUserName);

        //将Object转换成json字符串；
        String userString = JSON.toJSONString(user);

        StringEntity entity = new StringEntity(userString, "utf-8");

        //post请求是将参数放在请求体里面传过去的；这里将entity放入post请求体中
        httpPost.setEntity(entity);

        //设置头部信息
        httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
        httpPost.setHeader("Accept", "application/json;charset=utf-8");

        //响应模型
        CloseableHttpResponse response = null;

        try {
            //由客户端执行（发送）Post请求
            response = httpClient.execute(httpPost);

            //从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            String resp = null;
            JSONObject jsonObject = null;
            resp = EntityUtils.toString(responseEntity);
            jsonObject = JSON.parseObject(resp);
            logonToken = jsonObject.getString("logonToken");

        } catch (Exception e) {
            throw new SapBoException("在CIEDRankingDao中调用getLogonToken方法出错。用户信息：" + userString, e);
        } finally {
            try {
                //释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                throw new SapBoException("在CIEDRankingDao中调用getLogonToken方法时，释放资源出错！", e);
            }
        }
        log.debug("BO中" + boUserName + "用户成功取得logonToken：" + logonToken);
        return logonToken;
    }

    /**
     * 根据document的CUID取得documentId和描述信息
     *
     * @param logonToken
     * @param cuid
     * @return
     * @throws SapBoException
     */
    public JSONObject getDocumentIdAndDescription(String logonToken, String cuid) throws SapBoException, ParameterException {
        log.debug("开始执行CIEDRankingDao的getDocumentIdAndDescription方法");

        // 检查参数合法性
        if (logonToken == null || logonToken.trim().equalsIgnoreCase("")
                || cuid == null || cuid.trim().equalsIgnoreCase("")) {
            log.info("在CIEDRankingDao中调用getDocumentIdAndDescription方法时参数异常。logonToken = " + logonToken + ";cuid = " + cuid);
            throw new ParameterException("在CIEDRankingDao中调用getDocumentIdAndDescription方法时参数异常。logonToken = " + logonToken + ";cuid = " + cuid);
        }

        JSONObject document = new JSONObject();
        // 获取Http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 创建Get请求
        HttpGet httpGet = new HttpGet(boUrl + "/biprws/v1/infostore/" + cuid);

        // 设置头部信息
        httpGet.setHeader("X-SAP-LogonToken", logonToken);
        httpGet.setHeader("Accept", "application/json;charset=utf-8");

        //响应模型
        CloseableHttpResponse response = null;

        try {
            //配置信息
            RequestConfig requestConfig = RequestConfig.custom()
                    //设置连接超时时间（单位毫秒）
                    .setConnectTimeout(5000)
                    //设置请求超时时间（单位毫秒）
                    .setConnectionRequestTimeout(5000)
                    //socket读写超时时间（单位毫秒）
                    .setSocketTimeout(5000)
                    //设置是否允许重定向（默认为true）
                    .setRedirectsEnabled(true).build();

            //将上面配置信息，运用到这个Get请求里面
            httpGet.setConfig(requestConfig);

            //由客户端执行（发送）Get请求
            response = httpClient.execute(httpGet);

            //从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            String resp = null;
            JSONObject jsonObject = null;
            resp = EntityUtils.toString(responseEntity);
            jsonObject = JSON.parseObject(resp);
            document.put("documentId", jsonObject.get("id").toString());
            document.put("description", jsonObject.get("description").toString());

        } catch (Exception e) {
            throw new SapBoException("在CIEDRankingDao中调用getDocumentIdAndDescription方法出错！", e);
        } finally {
            try {
                //释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                throw new SapBoException("在CIEDRankingDao中调用getDocumentIdAndDescription方法时，释放资源出错！", e);
            }
        }
        log.debug("CIEDRankingDao中getDocumentIdAndDescription方法运行成功，取得信息：" + document);
        return document;
    }

    /**
     * 通过前端APP取到的用户名，刷新BO的webi报表
     *
     * @param logonToken
     * @param documentId
     * @param appUserName
     * @throws SapBoException
     */
    public void refreshDocumentWithParamenters(String logonToken, String documentId, String appUserName) throws SapBoException, ParameterException {
        log.debug("开始执行CIEDRankingDao的refreshDocumentWithParamenters方法");

        // 检查参数合法性
        if (logonToken == null || logonToken.equalsIgnoreCase("") || documentId == null || documentId.equalsIgnoreCase("") || appUserName == null || appUserName.equalsIgnoreCase("")) {
            log.info("在CIEDRankingDao中调用refreshDocumentWithParamenters方法时参数异常。logonToken = " + logonToken + ";" +
                    "documentId = " + documentId + ";userName = " + appUserName);
            throw new ParameterException("在CIEDRankingDao中调用refreshDocumentWithParamenters方法时参数异常。logonToken = " + logonToken + ";documentId = " + documentId + ";userName = " + appUserName);
        }

        //获取Http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 创建HttpPut请求
        HttpPut httpPut = new HttpPut(boUrl + "/biprws/raylight/v1/documents/" + documentId + "/parameters");

        //将参数放入请求体内部
        // TODO:根据名称取得id，然后放入下面id里面
        String selectUser =
                "{\"parameters\":{\"parameter\":[{\"id\":0, \"answer\":{\"values\":{\"value\":[\"" + appUserName +
                        "\"]}}}]}}";
        StringEntity entity = new StringEntity(selectUser, "utf-8");
        httpPut.setEntity(entity);

        //设置头部信息
        httpPut.setHeader("X-SAP-LogonToken", logonToken);
        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-Type", "application/json");

        //响应模型
        CloseableHttpResponse response = null;
        try {
            //由客户端执行（发送）Put请求
            httpClient.execute(httpPut);
        } catch (IOException e) {
            throw new SapBoException("在CIEDRankingDao中调用refreshDocumentWithParamenters方法出错！", e);
        } finally {
            try {
                //释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                throw new SapBoException("在CIEDRankingDao中调用refreshDocumentWithParamenters方法时，释放资源出错！", e);
            }
        }
        log.debug("CIEDRankingDao中refreshDocumentWithParamenters方法运行成功，成功刷新报表。");
    }

    /**
     * 取得规定report名的reportId
     *
     * @param logonToken
     * @param documentId
     * @return
     * @throws SapBoException
     */
    public JSONArray getReportId(String logonToken, String documentId) throws SapBoException, ParameterException {
        log.debug("开始执行CIEDRankingDao的getReportId方法");

        // 检查参数合法性
        if (logonToken == null || logonToken.equalsIgnoreCase("") || documentId == null || documentId.equalsIgnoreCase("")) {
            log.info("在CIEDRankingDao中调用getDocumentIdAndDescription方法时参数异常。logonToken = " + logonToken + ";cuid = " + documentId);
            throw new ParameterException("在CIEDRankingDao中调用getDocumentIdAndDescription方法时参数异常。logonToken = " + logonToken + ";cuid = " + documentId);
        }

        // 存放document里面的所有report的信息
        JSONArray reports = new JSONArray();

        //获取Http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        //创建Get请求
        HttpGet httpGet = new HttpGet(boUrl + "/biprws/raylight/v1/documents/" + documentId + "/reports");

        //设置头部信息
        httpGet.setHeader("X-SAP-LogonToken", logonToken);
        httpGet.setHeader("Accept", "application/json;charset=utf-8");

        //响应模型
        CloseableHttpResponse response = null;

        try {
            //配置信息
            RequestConfig requestConfig = RequestConfig.custom()
                    //设置连接超时时间（单位毫秒）
                    .setConnectTimeout(5000)
                    //设置请求超时时间（单位毫秒）
                    .setConnectionRequestTimeout(5000)
                    //socket读写超时时间（单位毫秒）
                    .setSocketTimeout(5000)
                    //设置是否允许重定向（默认为true）
                    .setRedirectsEnabled(true).build();

            //将上面配置信息，运用到这个Get请求里面
            httpGet.setConfig(requestConfig);

            //由客户端执行（发送）Get请求
            response = httpClient.execute(httpGet);

            //从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity == null) {
                throw new SapBoException("获取report信息失败！");
            }

            String resp = null;
            JSONObject jsonObject = null;
            resp = EntityUtils.toString(responseEntity);
            jsonObject = JSON.parseObject(resp);
            JSONArray jsonArray = jsonObject.getJSONObject("reports").getJSONArray("report");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject report = new JSONObject();
                report.put("name", jsonArray.getJSONObject(i).getString("name"));
                report.put("id", jsonArray.getJSONObject(i).getString("id"));
                reports.add(report);
            }

        } catch (Exception e) {
            throw new SapBoException("在CIEDRankingDao中调用getReportId方法出错！", e);
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                throw new SapBoException("在CIEDRankingDao中调用getReportId方法时，释放资源出错！", e);
            }
        }
        log.debug("CIEDRankingDao的getReportId方法运行成功。取得信息：" + reports);
        return reports;
    }

    /**
     * 取得具体document的report的所有BO内容
     *
     * @param logonToken
     * @param documentId
     * @param reportId
     * @return
     * @throws SapBoException
     */
    public String getBoReportContent(String logonToken, String documentId, String reportId) throws SapBoException, ParameterException {
        log.debug("开始执行getBoReportContent方法");

        // 检查参数合法性
        if (logonToken == null || logonToken.equalsIgnoreCase("") || documentId == null || documentId.equalsIgnoreCase("") || reportId == null || reportId.equalsIgnoreCase("")) {
            log.debug("在CIEDRankingDao中调用getDocumentIdAndDescription方法时参数异常。logonToken = " + logonToken + ";documentId = " + documentId + ";reportId = " + reportId);
            throw new ParameterException("在CIEDRankingDao中调用getDocumentIdAndDescription方法时参数异常。logonToken = " + logonToken + ";documentId = " + documentId + ";reportId = " + reportId);
        }

        // 存储从BO取到的所有数据
        String boReportContent = null;

        //获取Http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        //创建Get请求
        HttpGet httpGet =
                new HttpGet(boUrl + "/biprws/raylight/v1/documents/" + documentId + "/reports/" + reportId);

        //设置头部信息
        httpGet.setHeader("X-SAP-LogonToken", logonToken);
        httpGet.setHeader("Accept", "text/json;charset=utf-8");

        //响应模型
        CloseableHttpResponse response = null;

        try {
            //配置信息
            RequestConfig requestConfig = RequestConfig.custom()
                    //设置连接超时时间（单位毫秒）
                    .setConnectTimeout(5000)
                    //设置请求超时时间（单位毫秒）
                    .setConnectionRequestTimeout(5000)
                    //socket读写超时时间（单位毫秒）
                    .setSocketTimeout(5000)
                    //设置是否允许重定向（默认为true）
                    .setRedirectsEnabled(true).build();

            //将上面配置信息，运用到这个Get请求里面
            httpGet.setConfig(requestConfig);

            //由客户端执行（发送）Get请求
            response = httpClient.execute(httpGet);

            //从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity == null) {
                throw new SapBoException("在CIEDRankingDao中调用getBoContent方法得到的数据为空！");
            }
            boReportContent = EntityUtils.toString(responseEntity);

        } catch (Exception e) {
            throw new SapBoException("在CIEDRankingDao中调用getBoContent方法出错！", e);
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                throw new SapBoException("在CIEDRankingDao中调用getBoReportContent方法时，释放资源出错。", e);
            }
        }
        return boReportContent;
    }

    /**
     * 根据配置文件里面规定的bo报表数据块名称，取得对应数据块的id，以json格式返回｛name：id｝
     *
     * @param logonToken
     * @param reportId
     * @param reportId
     * @return
     */
    public JSONObject getBoDataBlockId(String logonToken, String documentId, String reportId) throws SapBoException, ParameterException {
        log.debug("开始执行CIEDRankingDao的getBoDataBlockId方法");

        // 检查参数合法性
        if (logonToken == null || logonToken.equalsIgnoreCase("") || documentId == null || documentId.equalsIgnoreCase("") || reportId == null || reportId.equalsIgnoreCase("")) {
            log.debug("在CIEDRankingDao中调用getBoDataBlockId方法时参数异常。logonToken = " + logonToken + ";documentId = " + documentId + ";reportId = " + reportId);
            throw new ParameterException("在CIEDRankingDao中调用getBoDataBlockId方法时参数异常。logonToken = " + logonToken + ";documentId = " + documentId + ";reportId = " + reportId);
        }

        JSONObject boDataBlockId = new JSONObject();
        //获取Http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        //创建Get请求
        HttpGet httpGet = new HttpGet(boUrl + "/biprws/raylight/v1/documents/" + documentId + "/reports/" + reportId +
                "/elements");

        //设置头部信息
        httpGet.setHeader("X-SAP-LogonToken", logonToken);
        httpGet.setHeader("Accept", "application/json;charset=utf-8");

        //响应模型
        CloseableHttpResponse response = null;
        try {
            //配置信息
            RequestConfig requestConfig = RequestConfig.custom()
                    //设置连接超时时间（单位毫秒）
                    .setConnectTimeout(5000)
                    //设置请求超时时间（单位毫秒）
                    .setConnectionRequestTimeout(5000)
                    //socket读写超时时间（单位毫秒）
                    .setSocketTimeout(5000)
                    //设置是否允许重定向（默认为true）
                    .setRedirectsEnabled(true).build();

            //将上面配置信息，运用到这个Get请求里面
            httpGet.setConfig(requestConfig);

            //由客户端执行（发送）Get请求
            response = httpClient.execute(httpGet);

            //从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            log.info(responseEntity.toString());
            if (responseEntity == null) {
                throw new SapBoException("在CIEDRankingDao中调用getBoDataBlockId方法获取数据为空");
            }
            String resp = null;
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            // 将取到的数据转成String类型，赋给resp
            resp = EntityUtils.toString(responseEntity);

            // 对特殊字符做替换处理，方便后续json操作
            resp = resp.replace("@", "A");

            jsonObject = JSON.parseObject(resp);
            jsonArray = jsonObject.getJSONObject("elements").getJSONArray("element");

            for (int i = 0; i < jsonArray.size(); i++) {
                if (jsonArray.getJSONObject(i).getString("name") != null) {
                    if (jsonArray.getJSONObject(i).getString("name").equals(formattedDateBlockName)) {
                        boDataBlockId.put(formattedDateBlockName, jsonArray.getJSONObject(i).getString("id"));
                    } else if (jsonArray.getJSONObject(i).getString("name").equals(mainDataBlockName)) {
                        boDataBlockId.put(mainDataBlockName, jsonArray.getJSONObject(i).getString("id"));
                    }
                }
            }
        } catch (Exception e) {
            log.warn("boDataBlockId的Get请求失败！");
            throw new SapBoException("在CIEDRankingDao中调用getBoDataBlockId方法出错！", e);
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                throw new SapBoException("在CIEDRankingDao中调用getBoDataBlockId方法时，释放资源出错。", e);
            }
        }
        log.debug("CIEDRankingDao的getBoDataBlockId运行成功。boDataBlockId:" + boDataBlockId);
        return boDataBlockId;
    }

    /**
     * 解析长json字符串，得到所需mainData信息，并以jsonArray格式返回（前端主要展示信息）
     *
     * @param boReportContent
     * @param mainDataId
     * @return
     */
    public JSONArray getMainDataOfBoContent(String boReportContent, String mainDataId) throws SapBoException, ParameterException {
        log.debug("开始执行CIEDRankingDao的getMainDataOfBoContent方法");

        // 检查参数合法性
        if (boReportContent == null || boReportContent.equalsIgnoreCase("") || mainDataId == null || mainDataId.equalsIgnoreCase("")) {
            log.debug("在CIEDRankingDao中调用getMainDataOfBoContent方法时参数异常。boReportContent = " + boReportContent + ";mainDataId = " + mainDataId);
            throw new ParameterException("在CIEDRankingDao中调用getMainDataOfBoContent方法时参数异常。boReportContent = " + boReportContent + ";mainDataId = " + mainDataId);
        }

        JSONArray mainData = new JSONArray();
        try {
            // 将String格式转换成JSONObject格式
            JSONObject jsonObject = JSONObject.parseObject(boReportContent);

            // 取得表的整体数据,jsonArray格式
            JSONArray table = jsonObject.getJSONArray("reports").getJSONObject(0).getJSONArray("child")
                    .getJSONObject(0).getJSONArray("child").getJSONObject(0).getJSONArray("child");

            // 表内所有数据
            JSONArray tableContents = new JSONArray();

            for (int i = 0; i < table.size(); i++) {
                if (table.getJSONObject(i).getString("bid").equals(mainDataId)) {
                    tableContents = table.getJSONObject(i).getJSONArray("tr");
                    break;
                }
            }

            // 主数据的行数、列数
            int columnNum, rowNum;
            columnNum = tableContents.getJSONObject(0).getJSONArray("td").size();
            rowNum = tableContents.size();

            //通过嵌套循环，将数据存到jsonArray
            // header用来存储Bo报表的表头数据
            JSONArray header = new JSONArray();

            for (int i = 0; i < rowNum; i++) {
                // bo报表的每一行数据，对应一个json格式的data
                JSONObject data = new JSONObject();

                for (int j = 0; j < columnNum; j++) {
                    JSONObject dataItemPre = tableContents.getJSONObject(i).getJSONArray("td")
                            .getJSONObject(j).getJSONArray("child").getJSONObject(0);

                    String dataItem = null;
                    //解决Bo里面的空数据导致的空指针异常
                    if (dataItemPre.getJSONObject("ct") != null) {
                        dataItem = dataItemPre.getJSONObject("ct").getString("value");
                    } else {
                        dataItem = "无数据";
                    }

                    // 将bo报表的头部信息作为指标数据的key值
                    if (i == 0) {
                        header.add(dataItem);
                    } else {
                        data.put(header.getString(j), dataItem);
                    }
                }
                if (i > 0) {
                    mainData.add(data);
                }
            }
        } catch (Exception e) {
            log.warn("在CIEDRankingDao中调用getMainDataOfBoContent时解析数据出错。");
            throw new SapBoException("在CIEDRankingDao中调用getMainDataOfBoContent时，解析原始json数据出错" + e);
        }
        log.debug("CIEDRankingDao的getMainDataOfBoContent运行成功。成功取得mainData数据");
        return mainData;
    }

    /**
     * 解析长json数据，得到所需formattedDate信息，并以String格式返回
     *
     * @param boReportContent
     * @param formattedDateId
     * @return
     */
    public String getFormattedDateOfBoContent(String boReportContent, String formattedDateId) throws SapBoException, ParameterException {
        log.debug("开始执行在CIEDRankingDao的getFormattedDateOfBoContent方法");

        // 检查参数合法性
        if (boReportContent == null || boReportContent.length() == 0 || formattedDateId == null || formattedDateId.length() == 0) {
            log.debug("在CIEDRankingDao中调用getMainDataOfBoContent方法时参数异常。boReportContent = " + boReportContent + ";formattedDateId = " + formattedDateId);
            throw new ParameterException("在CIEDRankingDao中调用getMainDataOfBoContent方法时参数异常。boReportContent = " + boReportContent + ";formattedDateId = " + formattedDateId);
        }

        String formattedDate = null;
        try {
            //将String格式转换成JSONObject格式
            JSONObject jsonObject = JSONObject.parseObject(boReportContent);

            //取得表的整体数据,jsonArray格式
            JSONArray table = jsonObject.getJSONArray("reports").getJSONObject(0).getJSONArray("child")
                    .getJSONObject(0).getJSONArray("child").getJSONObject(0).getJSONArray("child");

            //表内时间数据
            for (int i = 0; i < table.size(); i++) {
                if (table.getJSONObject(i).getString("bid").equals(formattedDateId)) {
                    formattedDate = table.getJSONObject(i).getJSONObject("ct").getString("value");
                }
            }
        } catch (Exception e) {
            log.warn("在CIEDRankingDao中调用getMainDataOfBoContent时解析数据出错。" + e);
            throw new SapBoException("在CIEDRankingDao中调用getMainDataOfBoContent时解析数据出错。" + e);
        }
        log.debug("在CIEDRankingDao中getFormattedDateOfBoContent运行成功。");
        return formattedDate;
    }
}
