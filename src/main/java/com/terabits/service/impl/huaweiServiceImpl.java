package com.terabits.service.impl;

import com.terabits.mapper.HuaweiMapper;
import com.terabits.service.HuaweiService;
import com.terabits.utils.DBTools;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

@Service("huaweiService")
public class huaweiServiceImpl implements HuaweiService {
    public int updateToken(String huaweitoken){
        SqlSession session = DBTools.getSession();
        HuaweiMapper huaweiMapper=session.getMapper(HuaweiMapper.class);
        int result=0;
        try {
            result=huaweiMapper.updateToken(huaweitoken);
            session.commit();
        }catch (Exception e){
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
        return result;
    }
}
