package com.yun.ssyx.acl.service.impl;

import com.yun.ssyx.acl.mapper.AdminRoleMapper;
import com.yun.ssyx.acl.service.AdminRoleService;
import com.yun.ssyx.model.acl.AdminRole;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRole> implements AdminRoleService {
}
