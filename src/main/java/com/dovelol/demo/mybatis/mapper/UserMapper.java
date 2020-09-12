package com.dovelol.demo.mybatis.mapper;

import com.dovelol.demo.mybatis.entity.User;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {

    /**
     * 通过id获取用户
     * @param id 用户id
     * @return 结果
     */
    @Select(value = "SELECT * FROM user WHERE id = #{id}")
    User getUserById(Long id);

}
