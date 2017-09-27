package com.terabits.mapper;

import com.terabits.meta.bo.TimeSpanBO;
import org.apache.ibatis.annotations.Param;
import com.terabits.meta.po.Device.TerminalPO;
import com.terabits.meta.bo.TerminalUpdateBO;
import com.terabits.meta.bo.CommunicationBO;
import com.terabits.meta.bo.NumberBO;
import com.terabits.meta.po.Device.OperationPO;
import org.springframework.security.access.method.P;

import java.util.List;

public interface DeviceMapper {


    /**
     * 根据displayid查询对应终端
     *
     * @param displayId
     * @return
     * @throws Exception
     */
    public TerminalPO selectOneTerminal(@Param("displayId") String displayId) throws Exception;

    /**
     * 通过imei号查询设备对象
     *
     * @param imei
     * @return
     * @throws Exception
     */
    public TerminalPO selectOneTerminalByImei(@Param("imei") String imei) throws Exception;

    /**
     * 返回全部终端数据
     *
     * @return
     * @throws Exception
     */
    public List<TerminalPO> selectAllTerminal() throws Exception;


    /**
     * 返回当前查询饮水机的地址
     *
     * @param displayId
     * @return
     * @throws Exception
     */
    public String selectLocation(@Param("displayId") String displayId) throws Exception;

    /**
     * 通过设备的imei更新设备的状态
     *
     * @param status
     * @param imei
     * @return
     * @throws Exception
     */
    public int updateTerminalStatusByImei(@Param("status") int status, @Param("imei") String imei) throws Exception;

    /**
     * 通过设备Id号更新设备的webId和displayId，用于添加设备
     *
     * @param webId
     * @param displayId
     * @return
     * @throws Exception
     */
    public int updateTerminalStatusById(@Param("webId") String webId, @Param("displayId") String displayId, @Param("id") int id) throws Exception;

    /**
     * 创建设备时插入在数据库里面插入设备的信息
     *
     * @param terminalPO
     * @return
     */
    public int insertTerminal(TerminalPO terminalPO);

    /**
     * 根据displayId删除设备
     *
     * @param displayId
     * @return
     * @throws Exception
     */
    public int deleteTerminal(@Param("displayId") String displayId) throws Exception;

    /**
     * 通过状态得到掉线的设备，状态码需要传进去
     *
     * @param state
     * @return
     * @throws Exception
     */
    public List<TerminalPO> selectOfflineTerminalByState(@Param("state") int state) throws Exception;

}
