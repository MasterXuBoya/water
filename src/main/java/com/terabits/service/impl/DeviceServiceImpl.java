package com.terabits.service.impl;

import com.terabits.mapper.AdminMapper;
import com.terabits.mapper.DeviceMapper;
import com.terabits.mapper.HeartBeatMapper;
import com.terabits.meta.po.Device.HeartBeatPO;
import com.terabits.meta.po.Device.TerminalPO;
import com.terabits.service.DeviceService;
import com.terabits.utils.DBTools;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("deviceService")
public class DeviceServiceImpl implements DeviceService {
    public TerminalPO selectOneTerminal(String displayId) {
        SqlSession session = DBTools.getSession();
        DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
        TerminalPO terminalPO = new TerminalPO();
        try {
            terminalPO = deviceMapper.selectOneTerminal(displayId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return terminalPO;
    }

    public TerminalPO selectOneTerminalByImei(String imei) {
        SqlSession session = DBTools.getSession();
        DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
        TerminalPO terminalPO = new TerminalPO();
        try {
            terminalPO = deviceMapper.selectOneTerminalByImei(imei);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return terminalPO;
    }


    public List<TerminalPO> selectAllTerminal() {
        SqlSession session = DBTools.getSession();
        DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
        List<TerminalPO> terminalPOList = new ArrayList<TerminalPO>();
        try {
            terminalPOList = deviceMapper.selectAllTerminal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return terminalPOList;
    }

    public String selectLocation(String displayId) {
        SqlSession session = DBTools.getSession();
        DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
        String location = "";
        try {
            location = deviceMapper.selectLocation(displayId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return location;
    }

    public int insertTerminal(TerminalPO terminalPO) {
        SqlSession session = DBTools.getSession();
        DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
        int result = 0;
        try {
            result = deviceMapper.insertTerminal(terminalPO);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return result;
    }

    public int deleteTerminal(String displayId) {
        SqlSession session = DBTools.getSession();
        DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
        int result = 0;
        try {
            result = deviceMapper.deleteTerminal(displayId);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return result;
    }

    public int insertHeartBeat(HeartBeatPO heartBeatPO) {
        SqlSession session = DBTools.getSession();
        HeartBeatMapper heartBeatMapper = session.getMapper(HeartBeatMapper.class);
        int result = 0;
        try {
            result = heartBeatMapper.insertHeartBeat(heartBeatPO);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return result;
    }

    public HeartBeatPO selectHeartBeat(String deviceId) {
        SqlSession session = DBTools.getSession();
        HeartBeatMapper heartBeatMapper = session.getMapper(HeartBeatMapper.class);
        HeartBeatPO result = new HeartBeatPO();
        try {
            result = heartBeatMapper.selectHeartBeat(deviceId);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return result;
    }

    public int updateTerminalStatusById(String webId, String displayId, int id) {
        SqlSession session = DBTools.getSession();
        DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
        int result = 0;
        try {
            result = deviceMapper.updateTerminalStatusById(webId, displayId, id);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return result;
    }

    public List<TerminalPO> selectOfflineTerminalByState(int state) {
        SqlSession session = DBTools.getSession();
        DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
        List<TerminalPO> OfflineTerminalPOS = new ArrayList<TerminalPO>();
        try {
            OfflineTerminalPOS = deviceMapper.selectOfflineTerminalByState(state);
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return OfflineTerminalPOS;
    }
}
