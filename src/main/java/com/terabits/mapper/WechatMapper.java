package com.terabits.mapper;

import com.terabits.meta.po.Wechat.AccessTokenPO;
import com.terabits.meta.po.Wechat.JsapiTicketPO;

import java.util.List;

public interface WechatMapper {
    //AccessToken////////////////////////////////////////////////////////
    //插入新的Token
    public int insertToken(AccessTokenPO accessTokenPO) throws Exception;

    //取回最后一个Token
    public AccessTokenPO selectLastToken() throws Exception;

    //取回全部Token
    public List<AccessTokenPO> selectAllToken() throws Exception;

    //JsAPITicket////////////////////////////////////////////////////////
    //插入新的token
    public int insertJsapi(JsapiTicketPO jsapiTicketPO) throws Exception;

    //取回最后一个token
    public JsapiTicketPO selectLastJsapi() throws Exception;

    //取回全部token
    public List<JsapiTicketPO> selectAllJsapi() throws Exception;
}
