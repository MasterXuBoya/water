package com.terabits.mapper;

import org.springframework.data.repository.query.Param;

public interface HuaweiMapper {
    public int updateToken(@Param("huaweitoken") String huaweitoken)throws Exception;
}
