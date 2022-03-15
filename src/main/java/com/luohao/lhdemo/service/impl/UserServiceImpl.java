package com.luohao.lhdemo.service.impl;

import com.luohao.lhdemo.entity.User;
import com.luohao.lhdemo.mapper.UserMapper;
import com.luohao.lhdemo.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 罗昊
 * @since 2022-03-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
