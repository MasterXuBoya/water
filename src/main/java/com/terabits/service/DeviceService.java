package com.terabits.service;

import com.terabits.meta.bo.CommunicationBO;
import com.terabits.meta.bo.NumberBO;
import com.terabits.meta.bo.TerminalUpdateBO;
import com.terabits.meta.bo.TimeSpanBO;
import com.terabits.meta.po.Device.HeartBeatPO;
import com.terabits.meta.po.Device.OperationPO;
import com.terabits.meta.po.Device.TerminalPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceService {

    public TerminalPO selectOneTerminal(String displayId);

    public TerminalPO selectOneTerminalByImei(String imei);

    public List<TerminalPO> selectAllTerminal();

    public String selectLocation(String displayId);

    public int insertTerminal(TerminalPO terminalPO);

    public int deleteTerminal(String displayId);

    public int insertHeartBeat(HeartBeatPO heartBeatPO);

    public HeartBeatPO selectHeartBeat(String deviceId);

    public int updateTerminalStatusById(String webId,String displayId,int id);

    public List<TerminalPO> selectOfflineTerminalByState(int state);

}
