package com.terabits.controller;

import com.auth0.jwt.internal.org.bouncycastle.asn1.ocsp.ResponseData;
import com.terabits.meta.po.Admin.AdminPO;
import com.terabits.meta.po.Device.TerminalPO;
import com.terabits.meta.po.Statistic.AuxcalPO;
import com.terabits.meta.po.Statistic.TotalPO;
import com.terabits.meta.po.User.ConsumeOrderPO;
import com.terabits.service.DeviceService;
import com.terabits.service.StatisticService;
import com.terabits.service.UserService;
import com.terabits.utils.JWT;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import java.util.Date;

//*********************************************记录管理**************************************************************
    /*jsonobject
    {
        record1：
        {
            todayflow:
            averflow:
            totalflow:
            hisaverflow:
        }
        record2：
        [
            {
                key:
                displayId:
                location:
                flow:
                hisfow:
            }
            {
                ……
            }
        ]
    }*/
@Controller
public class recordcontroller {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/record", method = RequestMethod.GET)
    public void record(@RequestHeader("Authorization") String token,
                       HttpServletResponse response) throws Exception {
        AdminPO adminPO = JWT.unsign(token, AdminPO.class);
        if (adminPO == null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", 0);
            response.getWriter().print(jsonObject);
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", 1);
            //***********************获得设备数量terminalNo****************
            int terminalNo;
            List<TerminalPO> terminalPOList = new ArrayList<TerminalPO>();
            terminalPOList = deviceService.selectAllTerminal();
            terminalNo = terminalPOList.size();
            //System.out.println(terminalNo);
            //*****************************************构造record2**********************************************************
            JSONArray record2 = new JSONArray();

            //*******************得到当天零点********************************
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date d = cal.getTime();
            String todayZero = format.format(d);
            //System.out.println(todayZero);
            //********************获取现在时间*******************************
            Date date = new Date();
            String instance = format.format(date);
            //System.out.println(instance);
            //************************************************************
            int i = 0;
            double sum;
            double totalflow = 0;
            String displayid;
            for (TerminalPO terminalPO : terminalPOList) {
                JSONObject jsonObject1 = new JSONObject();
                i++;
                jsonObject1.put("key", i);
                jsonObject1.put("displayId", terminalPO.getDisplayId());
                jsonObject1.put("location", terminalPO.getLocation());
                //*******************************************************************
                displayid = terminalPO.getDisplayId();
                //System.out.println(displayid);

                List<ConsumeOrderPO> consumeOrderPOList = new ArrayList<ConsumeOrderPO>();
                consumeOrderPOList = userService.selectConsumeOrderByTime(displayid, todayZero, instance);
                System.out.println("DeviceId:"+displayid);
                System.out.println(consumeOrderPOList.size());
                sum = 0;
                for (ConsumeOrderPO consumeOrderPO : consumeOrderPOList) {
                    sum += consumeOrderPO.getFlow();
                }
                jsonObject1.put("flow", sum);
                //**********************************************************************
                List<ConsumeOrderPO> consumeOrderPOList1 = new ArrayList<ConsumeOrderPO>();
                consumeOrderPOList1 = userService.selectConsumeOrderByDisplayId(displayid);
                //System.out.println(consumeOrderPOList1.size());
                sum = 0;
                for (ConsumeOrderPO consumeOrderPO : consumeOrderPOList1)
                    sum += consumeOrderPO.getFlow();
                totalflow += sum;
                jsonObject1.put("hisflow", sum);
                record2.add(jsonObject1);
            }
            //********************************************构造record1*******************************************************
            //*************************计算当天流量，平均流量*****************
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式
            String today = dateFormat.format(now);
            //System.out.println(today);
            AuxcalPO auxcalPO = statisticService.selectTodayAuxcal(today);
            double todayflow = auxcalPO.getFlow();
            double averflow = todayflow / terminalNo;
            //*************************计算历史流量，历史平均流量*****************
            double hisaverflow = totalflow / terminalNo;
            //*************************加入json*****************************
            JSONObject record1 = new JSONObject();
            record1.put("todayflow", todayflow);
            record1.put("averflow", averflow);
            record1.put("totalflow", totalflow);
            record1.put("hisaverflow", hisaverflow);
            
            //******************************************整合发送*************************************************************
            jsonObject.put("recordToday", record1);
            jsonObject.put("recordDevice", record2);
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().print(jsonObject);
        }
    }
}
