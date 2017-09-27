package com.terabits.service.impl;

import com.terabits.mapper.DeviceMapper;
import com.terabits.mapper.HeartBeatMapper;
import com.terabits.mapper.TerminalOfflineRecordMapper;
import com.terabits.meta.po.Device.HeartBeatPO;
import com.terabits.meta.po.Device.TerminalOfflinePO;
import com.terabits.meta.po.Device.TerminalPO;
import com.terabits.service.TerminalListenerService;
import com.terabits.utils.DBTools;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.terabits.constant.TerminalListenerConstant.OFFLINE_STATUS;
import static com.terabits.constant.TerminalListenerConstant.ONLINE_STATUS;
import static com.terabits.constant.TerminalListenerConstant.OVER_TIME;

@Service
public class TerminalListenerServiceImpl implements TerminalListenerService {


    public List<TerminalPO> selectAllTerminal() throws Exception {
        SqlSession session = DBTools.getSession();
        DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
        List<TerminalPO> terminalPOS = new ArrayList<TerminalPO>();
        try {
            terminalPOS = deviceMapper.selectAllTerminal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return terminalPOS;
    }

    public HeartBeatPO selectHeartBeatByDeviceId(String deviceId) throws Exception {
        SqlSession session = DBTools.getSession();
        HeartBeatMapper heartBeatMapper = session.getMapper(HeartBeatMapper.class);
        HeartBeatPO heartBeatPO = new HeartBeatPO();
        try {
            heartBeatPO = heartBeatMapper.selectHeartBeat(deviceId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return heartBeatPO;
    }

    /**
     * 根据imei号修改设备的状态
     *
     * @param status
     * @param imei
     * @return
     * @throws Exception
     */
    public int updateTerminalStatusByImei(int status, String imei) throws Exception {
        SqlSession session = DBTools.getSession();
        DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
        int result = 0;
        try {
            result = deviceMapper.updateTerminalStatusByImei(status, imei);
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
     * 计算时间是否超出给定范围,给定时间和现在时间差是否超时overtime，time直接将数据库中的gmtModified传进来就好了
     */
    public int calculateWhetherOffline(String time, int overtime) throws Exception {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        Date bein = dfs.parse(time);
        long timeSpan = (now.getTime() - bein.getTime()) / 1000; //timeSpan是秒数
        if (timeSpan > OVER_TIME) {
            return OFFLINE_STATUS;
        } else return ONLINE_STATUS;
    }

    public String calculateOfflineTime(String time) throws Exception {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        Date bein = dfs.parse(time);
        long seconds = (now.getTime() - bein.getTime()) / 1000; //timeSpan是秒数
        String timeStr = seconds + "秒";
        if (seconds > 60) {
            long second = seconds % 60;
            long min = seconds / 60;
            timeStr = min + "分" + second + "秒";
            if (min > 60) {
                min = (seconds / 60) % 60;
                long hour = (seconds / 60) / 60;
                timeStr = hour + "小时" + min + "分" + second + "秒";
                if (hour > 24) {
                    hour = ((seconds / 60) / 60) % 24;
                    long day = (((seconds / 60) / 60) / 24);
                    timeStr = day + "天" + hour + "小时" + min + "分" + second + "秒";
                }
            }
        }
        return timeStr;
    }

    public int insertOfflineRecord(TerminalOfflinePO terminalOfflinePO) throws Exception {
        SqlSession session = DBTools.getSession();
        TerminalOfflineRecordMapper terminalOfflineRecordMapper = session.getMapper(TerminalOfflineRecordMapper.class);
        int result = 0;
        try {
            result = terminalOfflineRecordMapper.insertTerminalOfflineRecord(terminalOfflinePO);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return result;
    }

    public int updateOfflineTimeByImei(String imei) throws Exception {
        SqlSession session = DBTools.getSession();
        TerminalOfflineRecordMapper terminalOfflineRecordMapper = session.getMapper(TerminalOfflineRecordMapper.class);
        int result = 0;
        try {
            result = terminalOfflineRecordMapper.updateOfflineTimeByImei(imei);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return result;
    }
}
