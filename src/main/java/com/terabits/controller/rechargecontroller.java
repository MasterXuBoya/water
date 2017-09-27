package com.terabits.controller;

import com.sun.xml.internal.fastinfoset.util.StringArray;
import com.terabits.meta.bo.TimeSpanBO;
import com.terabits.meta.po.Admin.AdminPO;
import com.terabits.meta.po.Admin.AdminRecordPO;
import com.terabits.meta.po.Statistic.AuxcalPO;
import com.terabits.meta.po.Statistic.TotalPO;
import com.terabits.meta.po.User.UserPO;
import com.terabits.service.AdminService;
import com.terabits.service.RechargeRecordService;
import com.terabits.service.StatisticService;
import com.terabits.service.UserService;
import com.terabits.utils.JWT;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class rechargecontroller {
    //*********************************************给所有用户充值**************************************************************
    /*
    JsonObject
    {
        adminname
        money:
    }*/
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private RechargeRecordService rechargeRecordService;

    @RequestMapping(value = "/recharge/getAll", method = RequestMethod.GET)
    public @ResponseBody
    JSONObject getUserInfor(@RequestHeader("Authorization") String token,
                            HttpServletResponse response) throws Exception {
        // 授权认证***
        AdminPO adminPO = JWT.unsign(token, AdminPO.class);
        JSONObject jsonObject = new JSONObject();
        if (adminPO == null) {
            jsonObject.put("status", 0);
            return jsonObject;
        } else {
        jsonObject.put("status", 1);
        List<UserPO> userPOS = userService.selectAllUser();
        JSONArray userInfoPOSWithoutHandle = JSONArray.fromObject(userPOS);
        JSONArray userInfoPOS = new JSONArray();
        for (int i = 0; i < userInfoPOSWithoutHandle.size(); i++) {
            JSONObject each = userInfoPOSWithoutHandle.getJSONObject(i);
            String openId = each.getString("openId");
            TimeSpanBO timeSpanBO = new TimeSpanBO();
            Double sumRecharge = rechargeRecordService.sumRechargePaymentByOpenIdAndTime(openId, timeSpanBO);//这里的timeSpanBO没有时间限制
            Double sumConsume = userService.sumConsumePaymentByOpenIdAndTime(openId, new TimeSpanBO());
            each.put("sumRecharge", sumRecharge);
            each.put("sumConsume", sumConsume);
            each.put("key", i + 1);
            switch (each.getInt("sex")) {
                case 1: {
                    each.put("gender", "male");
                    break;
                }
                case 2: {
                    each.put("gender", "female");
                    break;
                }
            }
            each.remove("sex");
            each.remove("id");
            each.remove("openId");//删除用户的openid,组成新的结果数组
            each.remove("gmtModified");//这个字段没有用到
            each.remove("headimgurl");//这个字段没有用到
            userInfoPOS.add(each);
        }
        jsonObject.put("userInfoPOS", userInfoPOS);
        return jsonObject;
    }
    }

    @RequestMapping(value = "/rechargeAll", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject rechargeAll(@RequestHeader("Authorization") String token,
                           @RequestParam(value = "adminName") String adminname,
                           @RequestParam(value = "money") String money,
                           HttpServletResponse response) throws Exception {
//        // 授权认证***
        AdminPO adminPO = JWT.unsign(token, AdminPO.class);
        JSONObject jsonObject = new JSONObject();
        if (adminPO == null) {
            jsonObject.put("status", 0);
            return jsonObject;
        } else {
            //标志
            int flag = 1;
            System.out.println(adminname + "  " + money);
            //*************************插入充值记录***************************
            AdminRecordPO adminRecordPO = new AdminRecordPO();
            adminRecordPO.setAdminName(adminname);
            double tmp = Double.valueOf(money).doubleValue();
            adminRecordPO.setMoney(tmp);
            adminRecordPO.setPhone("allUser");

            int result = adminService.insertAdminRecord(adminRecordPO);
            if (result == 0)
                flag = 0;
            //***********************查找测试成功*********************************
        /*List<AdminRecordPO> adminRecordPOList=new ArrayList<AdminRecordPO>();
        adminRecordPOList=adminService.selectAllAdminRecord();
        int i=adminRecordPOList.size();
        response.getWriter().print(i);*/
            //*************************对所有用户充值*************************************
            List<UserPO> userPOList = userService.selectAllUser();
            int userNo = userPOList.size();
            System.out.println("userNo:" + userNo);
            for (UserPO userPO : userPOList) {
                System.out.println(userPO);
                userPO.setPresent(userPO.getPresent() + Double.valueOf(money).doubleValue());
                result = userService.updateUser(userPO);
                if (result == 0)
                    flag = 0;
            }
            //*************************更新当日present***************************
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式
            String today = dateFormat.format(now);
            AuxcalPO auxcalPO = statisticService.selectTodayAuxcal(today);
            auxcalPO.setPresent(auxcalPO.getPresent() + userNo * Double.valueOf(money).doubleValue());
            result = statisticService.updateTodayAuxcal(auxcalPO);
            if (result == 0)
                flag = 0;
            //***************************更新总充值金额，总余额************************************
            TotalPO totalPO = statisticService.selectTotal();
            totalPO.setRecharge(totalPO.getRecharge() + userNo * Double.valueOf(money).doubleValue());
            totalPO.setRemain(totalPO.getRemain() + userNo * Double.valueOf(money).doubleValue());
            result = statisticService.updateTotal(totalPO);

            JSONObject rechargeStatus = new JSONObject();
            if (result == 0)
                flag = 0;
            if (flag == 1) {
                rechargeStatus.put("status", 1);
            } else {
                rechargeStatus.put("status", 0);
            }
            jsonObject.put("rechargeStatus", rechargeStatus);
            return jsonObject;
        }
    }

    //*******************************************************************************************************************************
/*给个人用户充值
    更新指定账户添加余额
    添加管理员操作记录
    更新今天的present
    更新历史recharge*/
    @RequestMapping(value = "/rechargePerson", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject rechargePerson(@RequestHeader("Authorization") String token,
                              @RequestParam(value = "adminName") String adminName,
                              @RequestParam(value = "money") String money,
                              @RequestParam(value = "phone[]") String[] phoneArray,
                              HttpServletResponse response) throws Exception {
//        // 授权认证***
        AdminPO adminPO = JWT.unsign(token, AdminPO.class);
        JSONObject jsonObject = new JSONObject();
        if (adminPO == null) {
            jsonObject.put("status", 0);
            return jsonObject;
        } else {
            int flag = 1, result;
            //System.out.println(adminName + "  " + money + "  " + str);
            String phoneRecord = "";  //插入到管理员充值记录中
            for (String phone : phoneArray) {
                phoneRecord = phone + "," + phoneRecord;
                System.out.println(adminName + "  " + money + "  " + phone);
                //*********************对指定用户添加余额******************
                UserPO userPO = userService.selectUserByNumber(phone);
                if (userPO != null) {
                	userPO.setPresent(userPO.getPresent() + Double.valueOf(money).doubleValue());
                    result = userService.updateUser(userPO);
                    if (result == 0)
                        flag = 0;
                }
            }
            //*******************插入充值记录********************
            AdminRecordPO adminRecordPO = new AdminRecordPO();
            adminRecordPO.setPhone(phoneRecord);
            adminRecordPO.setMoney(Double.valueOf(money).doubleValue());
            adminRecordPO.setAdminName(adminName);
            result = adminService.insertAdminRecord(adminRecordPO);
            if (result == 0)
                flag = 0;
            //***********************更新当日present*********************
            int userNo = phoneArray.length;
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式
            String today = dateFormat.format(now);
            AuxcalPO auxcalPO = statisticService.selectTodayAuxcal(today);
            auxcalPO.setPresent(auxcalPO.getPresent() + userNo * Double.valueOf(money).doubleValue());
            result = statisticService.updateTodayAuxcal(auxcalPO);
            if (result == 0)
                flag = 0;
            //************************更新total**************************
            TotalPO totalPO = statisticService.selectTotal();
            totalPO.setPresent(totalPO.getPresent() + userNo * Double.valueOf(money).doubleValue());
            totalPO.setRemain(totalPO.getRemain() + userNo * Double.valueOf(money).doubleValue());
            result = statisticService.updateTotal(totalPO);
            if (result == 0)
                flag = 0;
            if (flag == 1) {
                jsonObject.put("rechargeStatus", 1);
            } else {
                jsonObject.put("rechargeStatus", 0);
            }
            return jsonObject;
        }
    }

    @RequestMapping(value = "/recharge/adminRecord", method = RequestMethod.GET)
    public @ResponseBody
    JSONObject adminRecahrgeRecord(@RequestHeader("Authorization") String token,
                                   HttpServletResponse response) throws Exception {
//        // 授权认证***
        AdminPO adminPO = JWT.unsign(token, AdminPO.class);
        JSONObject jsonObject = new JSONObject();
        if (adminPO == null) {
            jsonObject.put("status", 0);
            return jsonObject;
        } else {
            //*******************取出充值记录********************
            List<AdminRecordPO> adminRecordPOS = adminService.selectAllAdminRecord();
            JSONArray adminRecordInfoWithoutHandle = JSONArray.fromObject(adminRecordPOS);
            JSONArray adminRecordInfo = new JSONArray();
            for (int i = 0; i < adminRecordInfoWithoutHandle.size(); i++) {
                JSONObject each = adminRecordInfoWithoutHandle.getJSONObject(i);
                each.put("key", i + 1);
                each.put("adminPhone", each.getString("phone"));
                each.remove("phone");
                each.remove("id");
                adminRecordInfo.add(each);
            }
            jsonObject.put("adminRecordInfo", adminRecordInfo);
            return jsonObject;
        }
    }
}
