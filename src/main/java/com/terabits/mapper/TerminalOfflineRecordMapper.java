package com.terabits.mapper;

import com.terabits.meta.bo.TimeSpanBO;
import com.terabits.meta.po.Device.TerminalOfflinePO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TerminalOfflineRecordMapper {
    /**
     * 插入设备掉线记录，imei号即可，时间自动生成
     *
     * @param terminalOfflineRecordPO
     * @return
     * @throws Exception，受影响的行数
     */
    public int insertTerminalOfflineRecord(TerminalOfflinePO terminalOfflineRecordPO) throws Exception;

    /**
     * 依据imei号码或者imei号码+时间来返回掉线记录列表
     *
     * @param imei
     * @param timeSpanBO
     * @return
     * @throws Exception
     */
    public List<TerminalOfflinePO> selectTerminalOfflineRecordByImeiOrTime(@Param("imei") String imei, @Param("timeSpanBO") TimeSpanBO timeSpanBO) throws Exception;

    /**
     * 依据imei号码或者imei号码+时间来是删除掉线记录列表
     * @param imei
     * @param timeSpanBO
     * @return
     * @throws Exception，受影响的行数
     */
    public int deleteTerminalOfflineRecordByImeiOrTime(@Param("imei") String imei, @Param("timeSpanBO") TimeSpanBO timeSpanBO) throws Exception;

    /**
     * 更新设备的掉线检查时间，获取设备的掉线时间长度
     * @param imei
     * @return
     * @throws Exception
     */
    public int updateOfflineTimeByImei(@Param("imei") String imei) throws Exception;
}
