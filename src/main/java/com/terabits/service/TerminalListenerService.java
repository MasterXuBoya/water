package com.terabits.service;

import com.terabits.meta.po.Device.HeartBeatPO;
import com.terabits.meta.po.Device.TerminalOfflinePO;
import com.terabits.meta.po.Device.TerminalPO;

import java.text.ParseException;
import java.util.List;

public interface TerminalListenerService {
    /**
     * 查询所有的设备数据，用于遍历所有设备
     *
     * @return
     * @throws Exception
     */
    public List<TerminalPO> selectAllTerminal() throws Exception;

    /***
     * 通过设备的deviceId查询到最新的心跳包字段
     * @param deviceId
     * @return
     * @throws Exception
     */
    public HeartBeatPO selectHeartBeatByDeviceId(String deviceId) throws Exception;

    /**
     * 通过设备的imei号更新设备的状态
     *
     * @param status
     * @param imei
     * @return
     * @throws Exception
     */
    public int updateTerminalStatusByImei(int status, String imei) throws Exception;

    /**
     * 计算时间是否超出给定范围,给定时间和现在时间差是否超时overtime，time直接将数据库中的gmtModified传进来就好了
     *
     * @param time
     * @param overtime
     * @return
     * @throws Exception
     */
    public int calculateWhetherOffline(String time, int overtime) throws Exception;

    /**
     * 输入掉线时刻，返回掉线时间
     *
     * @param time
     * @return
     * @throws Exception
     */
    public String calculateOfflineTime(String time) throws Exception;

    /**
     * 设备掉线插入设备掉线记录到数据库中
     *
     * @param terminalOfflinePO
     * @return
     * @throws Exception
     */
    public int insertOfflineRecord(TerminalOfflinePO terminalOfflinePO) throws Exception;

    /**
     * 更新设备的掉线检查时间，获取掉线时间的长度
     * @param imei
     * @return
     * @throws Exception
     */
    public int updateOfflineTimeByImei(String imei) throws Exception;
}
