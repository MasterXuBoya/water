package com.terabits.mapper;

import com.terabits.meta.po.CommandPO;
import org.apache.ibatis.annotations.Param;

public interface CommandMapper {
    /**
     * 插入命令，state默认是未执行的23
     */
    public int insertCommand(CommandPO commandPO) throws Exception;

    /**
     * 根据imei号和指令编号更新status状态
     */
    public int updateStatusByImeiAndCommandNo(@Param("imei")String imei,@Param("commandNo")String commandNo,@Param("status") int status) throws Exception;

    /**
     * 根据imei号和指令编号查询status状态
     */
    public int getStatusByImeiAndCommandNo(@Param("imei")String imei,@Param("commandNo")String commandNo) throws Exception;
}
