package com.yun.ssyx.acl.mapper;

import com.yun.ssyx.model.acl.RolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionRoleMapper extends BaseMapper<RolePermission> {
}
