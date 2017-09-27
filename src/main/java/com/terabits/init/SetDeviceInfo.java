package com.terabits.init;

import com.terabits.constant.HuaweiPlatformGlobal;
import com.terabits.utils.HttpsUtil;
import com.terabits.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

public class SetDeviceInfo {

    public static void main(String args[]) throws Exception {

        HttpsUtil httpsUtil = new HttpsUtil();
        httpsUtil.initSSLConfigForTwoWay(); //Two-Way Authentication

        String accessToken = login(httpsUtil); //Authentication，get token
        String appId = HuaweiPlatformGlobal.APP_ID;    //please replace the appId, when you use the demo.
        String deviceId = "fc040f18-090e-451c-8262-09b1aa6d2f35";  //please replace the deviceId, when you use the demo.
        String urlSetDeviceInfo = HuaweiPlatformGlobal.APP_URL + "/iocm/app/dm/v1.2.0/devices/" + deviceId;  //please replace the IP and Port, when you use the demo.
        String manufacturerId = "terabits";  //  please replace the manufacturerId, when you use the demo.
        String manufacturerName = "terabits";  //  please replace the manufacturerName, when you use the demo.
        String deviceType = "ElectricityMeter";  //please replace the deviceType, when you use the demo.
        String protocolType = "CoAP";
        String model = "001";  // please replace the model, when you use the demo.

        Map<String, Object> paramSetDeviceInfo = new HashMap<String, Object>();
        paramSetDeviceInfo.put("manufacturerId", manufacturerId);
        paramSetDeviceInfo.put("manufacturerName", manufacturerName);
        paramSetDeviceInfo.put("deviceType", deviceType);
        paramSetDeviceInfo.put("protocolType", protocolType);
        paramSetDeviceInfo.put("model", model);


        String jsonRequest = JsonUtil.jsonObj2Sting(paramSetDeviceInfo);

        Map<String, String> header = new HashMap<String, String>();
        header.put("app_key", appId);
        header.put("Authorization", "Bearer " + accessToken);

        String bodyModifyDeviceInfo = httpsUtil.doPutJsonForString(urlSetDeviceInfo, header, jsonRequest);

        System.out.println(bodyModifyDeviceInfo);
    }

    /**
     * Authentication，get token
     */
    @SuppressWarnings("unchecked")
    public static String login(HttpsUtil httpsUtil) throws Exception {

        String appId = HuaweiPlatformGlobal.APP_ID; // please replace the appId, when you use the demo.
        String secret = HuaweiPlatformGlobal.APP_PASSWORD; // please replace the secret, when you use the demo.
        String urlLogin = HuaweiPlatformGlobal.APP_URL + "/iocm/app/sec/v1.1.0/login"; // please replace the IP and Port, when you use the demo.

        Map<String, String> paramLogin = new HashMap<String, String>();
        paramLogin.put("appId", appId);
        paramLogin.put("secret", secret);

        String bodyLogin = httpsUtil.doPostFormUrlEncodedForString(urlLogin,
                paramLogin);
        System.out.println(bodyLogin);

        Map<String, String> data = new HashMap<String, String>();
        data = JsonUtil.jsonString2SimpleObj(bodyLogin, data.getClass());
        String accessToken = data.get("accessToken");
        return accessToken;
    }
}

