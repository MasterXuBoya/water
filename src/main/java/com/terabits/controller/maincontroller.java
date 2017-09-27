package com.terabits.controller;

import com.auth0.jwt.internal.org.bouncycastle.asn1.ocsp.ResponseData;
import com.terabits.meta.bo.TimeSpanBO;
import com.terabits.meta.po.Admin.AdminPO;
import com.terabits.meta.po.Device.TerminalPO;
import com.terabits.service.*;
import com.terabits.utils.JWT;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.terabits.constant.TerminalListenerConstant.OFFLINE_STATUS;

@Controller
public class maincontroller {

    @Autowired
    private AdminService adminService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private UserService userService;
    @Autowired
    private RechargeRecordService rechargeRecordService;
    @Autowired
    private TerminalListenerService terminalListenerService;


    /**
     * 登录界面控制程序，依据用户的登录名和密码生成token签发给客户端
     *
     * @param name
     * @param password
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject login(@RequestParam(value = "name") String name,
                     @RequestParam(value = "password") String password,
                     HttpServletResponse response) throws Exception {
        System.out.println("name:" + name + " password:" + password);
        AdminPO adminPO = adminService.selectAdmin(name);
        System.out.println(adminPO);
        if (adminPO != null && adminPO.getPassword().equals(password)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("admin", adminPO);
            String token = JWT.sign(adminPO, 48 * 1800L * 1000L); //签发半小时的token
            System.out.println(token);
            jsonObject.put("token", token);
            jsonObject.put("status", 1);
            return jsonObject;
            //response.getWriter().print(jsonObject);
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", 0);
            return jsonObject;
            //response.getWriter().print(jsonObject );
        }
    }

    @RequestMapping(value = "/mainpage", method = RequestMethod.GET)
    public @ResponseBody
    JSONObject mainpage(@RequestHeader("Authorization") String clientToken) throws Exception {

        AdminPO adminPO = JWT.unsign(clientToken, AdminPO.class);
        System.out.println(adminPO);
        if (adminPO == null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", 0);
            return jsonObject;
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", 0);

            ////////////////////////////////////////系统概览/////////////////////////////////////////////////////

            JSONObject head = new JSONObject();
            int terminalNumber = deviceService.selectAllTerminal().size();  //获取设备总数
            int userNumber = userService.selectAllUser().size();   //获取用户总数
            Double rechargeNumber = rechargeRecordService.sumRechargePaymentByOpenIdAndTime("", new TimeSpanBO());
            head.put("terminalNumber", terminalNumber);
            head.put("userNumber", userNumber);
            head.put("rechargeNumber", rechargeNumber);

            ////////////////////////////////////////昨日统计/////////////////////////////////////////////////////

            int userNumberYesterday = userService.selectNewUserByTime(new TimeSpanBO().setTimeYesterday());
            Double rechargeNumberYesterday = rechargeRecordService.sumRechargePaymentByOpenIdAndTime("", new TimeSpanBO().setTimeYesterday());
            Double flowNumberYesterday = userService.sumFlowByOpenIdAndTime("", new TimeSpanBO().setTimeYesterday());
            Double consumeNumberYesterday = userService.sumConsumePaymentByOpenIdAndTime("", new TimeSpanBO().setTimeYesterday());
            head.put("userNumberYesterday", userNumberYesterday);
            head.put("rechargeNumberYesterday", rechargeNumberYesterday);
            head.put("flowNumberYesterday", flowNumberYesterday);
            head.put("consumeNumberYesterday", consumeNumberYesterday);

            jsonObject.put("head", head);

            ////////////////////////////////////////设备报警/////////////////////////////////////////////////////

            List<TerminalPO> offlineTerminalPOS = deviceService.selectOfflineTerminalByState(OFFLINE_STATUS);
            JSONArray offlineTerminal = new JSONArray();
            for (TerminalPO eachTerminal : offlineTerminalPOS) {
                JSONObject each = new JSONObject();
                each.put("displayId", eachTerminal.getDisplayId());
                each.put("location", eachTerminal.getLocation());
                each.put("simId", eachTerminal.getSimId());
                each.put("imei", eachTerminal.getImei());
                String lastConnectTime = deviceService.selectHeartBeat(eachTerminal.getDeviceId()).getGmtModified();
                each.put("lastConnectTime", lastConnectTime);
                each.put("offlineTime", terminalListenerService.calculateOfflineTime(lastConnectTime));
                offlineTerminal.add(each);
            }
            jsonObject.put("offlineTerminal", offlineTerminal);
            return jsonObject;
        }
    }
}
