package com.terabits.init;

import com.terabits.constant.HuaweiPlatformGlobal;
import com.terabits.utils.HttpsUtil;
import com.terabits.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

public class RegisterDirectlyConnectedDevice {

	public static void main(String args[]) throws Exception {

		HttpsUtil httpsUtil = new HttpsUtil();
		httpsUtil.initSSLConfigForTwoWay(); // Two-Way Authentication

		String accessToken = login(httpsUtil); //Authentication，get token
		String appId = HuaweiPlatformGlobal.APP_ID; // please replace the appId, when you use the demo.
		String urlReg = HuaweiPlatformGlobal.APP_URL + "/iocm/app/reg/v1.2.0/devices"; //Repalce your url.
		String verifyCode = "863703030679133"; //   please replace the verifyCode, when you use the demo.
		String nodeId = "863703030679133"; // please replace the nodeId, when you use the demo.
		String EndUserId = "currentuser"; // please replace the currentuser, when you use the demo.

		Map<String, Object> paramReg = new HashMap<String, Object>();
		paramReg.put("verifyCode", verifyCode.toUpperCase());
		paramReg.put("nodeId", nodeId.toUpperCase());
		paramReg.put("endUserId", EndUserId);
		paramReg.put("timeout", 0); //   please replace the timeout, when you use the demo.

		String jsonRequest = JsonUtil.jsonObj2Sting(paramReg);

		Map<String, String> header = new HashMap<String, String>();
		header.put("app_key", appId);
		header.put("Authorization", "Bearer " + accessToken);

		String bodyReg = httpsUtil.doPostJsonForString(urlReg, header,
				jsonRequest);

		System.out.println(bodyReg);
	}

	/**
	 * Authentication，get token
	 * */
	@SuppressWarnings("unchecked")
	public static String login(HttpsUtil httpsUtil) throws Exception {

		String appId =  HuaweiPlatformGlobal.APP_ID; // please replace the appId, when you use the demo.
		String secret = HuaweiPlatformGlobal.APP_PASSWORD; // please replace the secret, when you use the demo.
		String urlLogin =HuaweiPlatformGlobal.APP_URL + "/iocm/app/sec/v1.1.0/login"; //please replace the IP and Port, when you use the demo.

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
