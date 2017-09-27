package com.terabits.controller;

import com.terabits.meta.bo.TimeSpanBO;
import com.terabits.meta.po.Admin.AdminPO;
import com.terabits.meta.po.User.ConsumeOrderPO;
import com.terabits.meta.po.User.RechargeOrderPO;
import com.terabits.meta.po.User.UserPO;
import com.terabits.service.DeviceService;
import com.terabits.service.RechargeRecordService;
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
import java.util.List;

@Controller
public class UserInfoController {
    @Autowired
    private UserService userService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private RechargeRecordService rechargeRecordService;

    /**
     * 通过手机号码查询用户信息
     *
     * @param token
     * @param phone
     * @param beginTime
     * @param endTime
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/userinfo/search/phone", method = RequestMethod.GET)
    public void deleteById(@RequestHeader("Authorization") String token,
                           @RequestParam("phone") String phone,
                           @RequestParam("beginTime") String beginTime,
                           @RequestParam("endTime") String endTime,
                           HttpServletResponse response) throws Exception {
        //验证客户端Token是否满足AdminPO的要求
        AdminPO adminPO = JWT.unsign(token, AdminPO.class);
        if (adminPO == null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", 0);
            response.getWriter().print(jsonObject);
            return;
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", 1);

            //执行查询，返回用户基本信息查询结果
            UserPO userPO = new UserPO();
            userPO = userService.selectUserByNumber(phone);
            JSONObject userInfo = JSONObject.fromObject(userPO);
            userInfo.remove("openId");//删除用户的openid
            jsonObject.put("userInfo", userInfo);

            String openId = userPO.getOpenId();
            TimeSpanBO timeSpanBO = new TimeSpanBO(beginTime, endTime);

            //执行用户消费订单查询
            List<ConsumeOrderPO> consumeOrderPOS = userService.selectConsumeOrderByOpenIdAndTime(openId, timeSpanBO);
            JSONArray consumeInfoWithOpenId = JSONArray.fromObject(consumeOrderPOS);
            JSONArray consumeInfo = new JSONArray();
            for (int i = 0; i < consumeInfoWithOpenId.size(); i++) {
                JSONObject each = consumeInfoWithOpenId.getJSONObject(i);
                if (each.containsKey("openId")) {
                    each.remove("openId");//删除用户的openid,组成新的结果数组
                }
                if (each.containsKey("gmtModified")) {
                    each.remove("gmtModified");//这个字段没有用到
                }
                each.put("key", i + 1);
                each.remove("id");
                String deviceLocation = deviceService.selectLocation((String) each.get("displayId"));//将消费订单上的displayId转换为设备所在的位置
                each.put("deviceLocation", deviceLocation);
                consumeInfo.add(each);
            }
            jsonObject.put("consumeInfo", consumeInfo);

            //执行用户充值记录查询
            List<RechargeOrderPO> rechargeOrderPOS = rechargeRecordService.selectRechargeRecordByOpenIdAndTime(openId, timeSpanBO);
            JSONArray rechargeInfoWithOpenId = JSONArray.fromObject(rechargeOrderPOS);
            JSONArray rechargeInfo = new JSONArray();
            for (int i = 0; i < rechargeInfoWithOpenId.size(); i++) {
                JSONObject each = rechargeInfoWithOpenId.getJSONObject(i);
                if (each.containsKey("openId")) {
                    each.remove("openId");//删除用户的openid,组成新的结果数组
                }
                if (each.containsKey("gmtModified")) {
                    each.remove("gmtModified");//这个字段没有用到
                }
                each.put("key", i + 1);
                each.remove("id");
                rechargeInfo.add(each);
            }
            jsonObject.put("rechargeInfo", rechargeInfo);
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().print(jsonObject);
        }
    }
}
