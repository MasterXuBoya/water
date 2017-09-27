package com.terabits.service.impl;

import com.terabits.mapper.FeedbackMapper;
import com.terabits.mapper.RechargeOrderMapper;
import com.terabits.meta.bo.TimeSpanBO;
import com.terabits.meta.po.FeedbackPO;
import com.terabits.meta.po.User.RechargeOrderPO;
import com.terabits.service.RechargeRecordService;
import com.terabits.utils.DBTools;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RechargeRecordServiceImpl implements RechargeRecordService {

    public List<RechargeOrderPO> selectRechargeRecordByOpenIdAndTime(String openId, TimeSpanBO timeSpanBO) throws Exception {
        SqlSession session = DBTools.getSession();
        RechargeOrderMapper rechargeOrderMapper = session.getMapper(RechargeOrderMapper.class);
        List<RechargeOrderPO> rechargeOrderPOS = new ArrayList<RechargeOrderPO>();
        try {
            rechargeOrderPOS = rechargeOrderMapper.selectPaymentByOpenIdAndTime(openId, timeSpanBO);
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return rechargeOrderPOS;
    }

    public Double sumRechargePaymentByOpenIdAndTime(String openId, TimeSpanBO timeSpanBO) throws Exception {
        SqlSession session = DBTools.getSession();
        RechargeOrderMapper rechargeOrderMapper = session.getMapper(RechargeOrderMapper.class);
        Double sum = 0.0;
        try {
            sum = rechargeOrderMapper.sumRechargePaymentByOpenIdAndTime(openId, timeSpanBO);
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return sum;
    }
}
