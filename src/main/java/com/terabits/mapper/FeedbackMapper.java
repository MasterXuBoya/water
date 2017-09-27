package com.terabits.mapper;

import com.terabits.meta.bo.TimeSpanBO;
import com.terabits.meta.po.FeedbackPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;

import java.util.List;

public interface FeedbackMapper {
    /**
     * 插入反馈记录
     *
     * @param feedbackPO
     * @return 受影响的行数
     */
    public int insertFeedback(FeedbackPO feedbackPO);

    /**
     * 通过id更新反馈的状态
     *
     * @param status,id
     * @return 受影响的行数
     */
    public int updateFeedbackStatusById(@Param("status") int status, @Param("id") int id);

    /**
     * 通过电话号码和时间来查询反馈
     *
     * @param phone
     * @return该电话号码相关的所有反馈
     */
    public List<FeedbackPO> selectFeedbackByPhoneAndTime(@Param("phone") String phone, @Param("timeSpanBO")TimeSpanBO timeSpanBO);

    /**
     * 通过电话号码删除反馈
     *
     * @param id
     * @return 受影响的行数
     */
    public int deleteFeedbackById(@Param("id") int id);

    /**
     * 通过时间段删除已经解决的反馈
     *
     * @param timeSpanBO
     * @return 受影响的行数
     */
    public int deleteFeedbackByTimeOnlySolved(@Param("timeSpanBO") TimeSpanBO timeSpanBO, @Param("status") int status);

}
