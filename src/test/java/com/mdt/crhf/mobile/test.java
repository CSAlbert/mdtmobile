package com.mdt.crhf.mobile;

import com.alibaba.fastjson.JSONObject;
import org.hibernate.validator.internal.util.logging.Log_$logger;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chulang
 * @version 1.0
 * @date 2019/11/8 16:19
 * @Description TODO
 */

public class test {

    private final Logger log = LoggerFactory.getLogger(test.class);

    public static void main(String[] args) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","张三");
        jsonObject.put("age","25");

        String profession = jsonObject.getString("profession");

        System.out.println(profession);

    }
}
