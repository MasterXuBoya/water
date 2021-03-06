package com.terabits.service.impl;

import com.terabits.mapper.StatisticMapper;
import com.terabits.mapper.UserMapper;
import com.terabits.meta.bo.TimeSpanBO;
import com.terabits.meta.po.User.ConsumeOrderPO;
import com.terabits.meta.po.User.RechargeOrderPO;
import com.terabits.meta.po.User.UserPO;
import com.terabits.service.UserService;
import com.terabits.utils.DBTools;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//增删改 session.commit(),session.rollback(),session.close()
//查询操作，session.close()
@Service("userService")
public class UserServiceImpl implements UserService {
    /**
     * 通过设备编号查询订单记录
     */
    public List<ConsumeOrderPO> selectConsumeOrderByDisplayId(String displayId) {
        SqlSession session = DBTools.getSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        List<ConsumeOrderPO> consumeOrderPOList = new ArrayList<ConsumeOrderPO>();
        try {
            consumeOrderPOList = userMapper.selectConsumeOrderByDisplayId(displayId);
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return consumeOrderPOList;
    }

    /**
     * 查询某段时间消费记录
     */
    public List<ConsumeOrderPO> selectConsumeOrderByTime(String displayId, String beginTime, String endTime) {
        SqlSession session = DBTools.getSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        List<ConsumeOrderPO> consumeOrderPOList = new ArrayList<ConsumeOrderPO>();
        try {
            consumeOrderPOList = userMapper.selectConsumeOrderByTime(displayId, beginTime, endTime);
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return consumeOrderPOList;
    }

    /**
     * 通过电话号码查询用户信息
     */
    public UserPO selectUserByNumber(String phone) {
        SqlSession session = DBTools.getSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        UserPO userPO = new UserPO();
        try {
            userPO = userMapper.selectUserByNumber(phone);
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return userPO;
    }

    /**
     * 通过时间和openid查询用户的消费信息
     *
     * @param openId
     * @param timeSpanBO
     * @return
     */
    public List<ConsumeOrderPO> selectConsumeOrderByOpenIdAndTime(String openId, TimeSpanBO timeSpanBO) {
        SqlSession session = DBTools.getSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        List<ConsumeOrderPO> consumeOrderPOS = new ArrayList<ConsumeOrderPO>();
        try {
            System.out.println("-----------------------------------");
            System.out.println(openId);
            System.out.println(timeSpanBO);
            System.out.println("-----------------------------------");
            consumeOrderPOS = userMapper.selectConsumeOrderByOpenIdAndTime(openId, timeSpanBO.getBeginTime(), timeSpanBO.getEndTime());
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return consumeOrderPOS;
    }

    /**
     * 更新用户信息，用作更新余额
     */
    public int updateUser(UserPO userPO) {
        SqlSession session = DBTools.getSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        int result = 0;
        try {
            result = userMapper.updateUser(userPO);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return result;
    }

    /**
     * 返回所有用户信息
     */
    public List<UserPO> selectAllUser() {
        SqlSession session = DBTools.getSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        List<UserPO> userPOList = new ArrayList<UserPO>();
        try {
            userPOList = userMapper.selectAllUser();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return userPOList;
    }

    public List<RechargeOrderPO> selectAllRechargeRecord(TimeSpanBO timeSpanBO) {
        SqlSession session = DBTools.getSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        List<RechargeOrderPO> rechargeRecordPOList = new ArrayList<RechargeOrderPO>();
        try {
            rechargeRecordPOList = userMapper.selectAllRechargeRecord(timeSpanBO);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return rechargeRecordPOList;
    }

    public List<ConsumeOrderPO> selectAllConsumption(TimeSpanBO timeSpanBO) {
        SqlSession session = DBTools.getSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        List<ConsumeOrderPO> consumeOrderPOList = new ArrayList<ConsumeOrderPO>();
        try {
            consumeOrderPOList = userMapper.selectAllConsumption(timeSpanBO);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return consumeOrderPOList;
    }

    public int selectNewUserByTime(TimeSpanBO timeSpanBO) {
        SqlSession session = DBTools.getSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        int count = 0;
        try {
            count = userMapper.selectNewUserByTime(timeSpanBO);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return count;
    }

    public Double sumConsumePaymentByOpenIdAndTime(String openId, TimeSpanBO timeSpanBO) {
        SqlSession session = DBTools.getSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        Double sum = 0.0;
        try {
            sum = userMapper.sumConsumePaymentByOpenIdAndTime(openId, timeSpanBO);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return sum;
    }

    public Double sumFlowByOpenIdAndTime(String openId, TimeSpanBO timeSpanBO) {
        SqlSession session = DBTools.getSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        Double sum = 0.0;
        try {
            sum = userMapper.sumFlowByOpenIdAndTime(openId, timeSpanBO);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return sum;
    }

}
