package com.terabits.controller;

import com.terabits.meta.bo.TimeSpanBO;
import com.terabits.meta.po.Admin.AdminPO;
import com.terabits.meta.po.FeedbackPO;
import com.terabits.service.FeedbackService;
import com.terabits.utils.JWT;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static com.terabits.constant.FeedbackCostant.SOLVED_STATUS;

@Controller
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    /**
     * 获取反馈信息列表
     *
     * @param phone
     * @param beginTime
     * @param endTime
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/feedback/get", method = RequestMethod.GET)
    public void getFeedback(@RequestHeader("Authorization") String token,
                            @RequestParam("beginTime") String beginTime,
                            @RequestParam("endTime") String endTime,
                            @RequestParam("phone") String phone,
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

            //反馈内容读取，回复
            TimeSpanBO timeSpanBO = new TimeSpanBO(beginTime, endTime);
            List<FeedbackPO> feedbackPOS = feedbackService.selectFeedbackByPhoneAndTime(phone, timeSpanBO);
            JSONArray feedbackContentUnhandle = JSONArray.fromObject(feedbackPOS);
            JSONArray feedbackContent = new JSONArray();
            for (int i = 0; i < feedbackContentUnhandle.size(); i++) {
                JSONObject each = feedbackContentUnhandle.getJSONObject(i);
                each.put("key", each.getInt("id"));
                each.remove("id");
                feedbackContent.add(each);
            }
            jsonObject.put("feedbackContent", feedbackContent);
            response.getWriter().print(jsonObject);
        }
    }

    /**
     * 阅读完成反馈，标记状态为解决，66，传入id，修改数据字段
     *
     * @param id
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/feedback/updateStatus", method = RequestMethod.POST)
    public void updateStatusById(@RequestHeader("Authorization") String token,
                                 @RequestParam("id") int id,
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

            //执行更新操作，返回更新结果
            int result = feedbackService.updateFeedbackStatusById(SOLVED_STATUS, id);
            jsonObject.put("updateResult", result);
            response.getWriter().print(jsonObject);
        }
    }

    /**
     * 通过id删除反馈记录
     *
     * @param id
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/feedback/delete/id", method = RequestMethod.POST)
    public void deleteById(@RequestHeader("Authorization") String token,
                           @RequestParam("id") int id,
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

            //执行删除操作，返回更新结果
            int result = feedbackService.deleteFeedbackById(id);
            jsonObject.put("deleteResult", result);
            response.getWriter().print(jsonObject);
        }
    }

    @RequestMapping(value = "/feedback/delete/time", method = RequestMethod.POST)
    public void deleteFeedbackByTimeOnlySolved(@RequestHeader("Authorization") String token,
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

            //执行删除操作，返回删除行数结果
            TimeSpanBO timeSpanBO = new TimeSpanBO(beginTime, endTime);
            int result = feedbackService.deleteFeedbackByTimeOnlySolved(timeSpanBO, SOLVED_STATUS);
            jsonObject.put("updateResult", result);
            response.getWriter().print(jsonObject);
        }
    }
}
