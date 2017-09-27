package com.terabits.controller;

import com.terabits.constant.Constant;
import com.terabits.meta.bo.TimeSpanBO;
import com.terabits.meta.po.Admin.AdminPO;
import com.terabits.meta.po.User.ConsumeOrderPO;
import com.terabits.meta.po.User.RechargeOrderPO;
import com.terabits.meta.po.User.UserPO;
import com.terabits.service.UserService;
import com.terabits.utils.JWT;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class usertotalcontroller {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/usertotal", method = RequestMethod.GET)
    public void usertotal(@RequestHeader("Authorization") String token,
                          HttpServletResponse response) throws Exception {
        AdminPO adminPO = JWT.unsign(token, AdminPO.class);
        if (adminPO == null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", 0);
            response.getWriter().print(jsonObject);
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", 1);
            //**********************************************************************************************************
            JSONObject userTotal1 = new JSONObject();

            List<UserPO> userPOList = userService.selectAllUser();
            int userNo = userPOList.size();
            System.out.println("userNo:" + userNo);
            userTotal1.put("userTotalNo", userNo);
            //*****************************************************
            SimpleDateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatSecond = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = new Date();
            String instance = formatSecond.format(date1);

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, -30);//向前追溯i天
            Date d = c.getTime();
            String tmp = formatSecond.format(d);
            TimeSpanBO timeSpanBO = new TimeSpanBO();
            timeSpanBO.setBeginTime(tmp);
            timeSpanBO.setEndTime(instance);
            List<RechargeOrderPO> rechargeRecordPOList = userService.selectAllRechargeRecord(timeSpanBO);
            double sum = 0;
            for (RechargeOrderPO rechargeRecordPO : rechargeRecordPOList) {
                sum += rechargeRecordPO.getPayment();
            }

            Double monthAverRecharge = sum / userNo;
            userTotal1.put("monthAverRecharge", monthAverRecharge);


            //*****************************************************

            TimeSpanBO timeSpanBO1 = new TimeSpanBO();
            timeSpanBO1.setBeginTime(Constant.StartTime);
            timeSpanBO1.setEndTime(instance);
            List<RechargeOrderPO> rechargeRecordPOList1 = userService.selectAllRechargeRecord(timeSpanBO1);
            sum = 0;
            for (RechargeOrderPO rechargeRecordPO : rechargeRecordPOList1) {
                sum += rechargeRecordPO.getPayment();
            }
            Double hisAverRecharge = sum / userNo;
            userTotal1.put("hisAverRecharge", hisAverRecharge);


            //*****************************************************
            List<ConsumeOrderPO> consumeOrderPOList = userService.selectAllConsumption(timeSpanBO);
            sum = 0;
            for (ConsumeOrderPO consumeOrderPO : consumeOrderPOList) {
                sum += consumeOrderPO.getPayment();
            }
            Double monthAverConsume = sum / userNo;
            userTotal1.put("monthAverConsume", monthAverConsume);


            //*****************************************************
            List<ConsumeOrderPO> consumeOrderPOList1 = userService.selectAllConsumption(timeSpanBO1);
            sum = 0;
            for (ConsumeOrderPO consumeOrderPO : consumeOrderPOList) {
                sum += consumeOrderPO.getPayment();
            }
            Double hisAverConsume = sum / userNo;
            userTotal1.put("hisAverConsume", hisAverConsume);


            System.out.println(userTotal1);
            //**********************************************************************************************************
            JSONObject userTotal2 = new JSONObject();

            String[] date = new String[30];
            int[] newUserNo = new int[30];


            String beginTime, endTime;
            String dateArray[] = new String[31];
            for (int i = 0; i <= 30; i++) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.MILLISECOND, 0);
                cal.add(Calendar.DATE, -(30 - i));//向前追溯i天
                d = cal.getTime();
                dateArray[i] = formatSecond.format(d);
                if (i < 30)
                    date[i] = formatDay.format(d);
            }


            for (int i = 0; i < 30; i++) {
                System.out.print(dateArray[i] + " ");
            }
            for (int i = 0; i < 30; i++) {
                beginTime = dateArray[i];
                endTime = dateArray[i + 1];
                TimeSpanBO timeSpanBO2 = new TimeSpanBO();
                timeSpanBO2.setBeginTime(beginTime);
                timeSpanBO2.setEndTime(endTime);
                System.out.println(timeSpanBO2);
                newUserNo[i] = userService.selectNewUserByTime(timeSpanBO2);
            }


            userTotal2.put("date", date);
            userTotal2.put("newUserNo", newUserNo);

            System.out.println(userTotal2);
            //**********************************************************************************************************
            jsonObject.put("userTotalUp", userTotal1);
            jsonObject.put("userTotalDown", userTotal2);
            //String tt=userTotal1.getString("userTotalNo");
            //JSONObject mdm=jsonObject.getJSONObject("userTotal1");
            response.getWriter().print(jsonObject);
        }
    }

}
