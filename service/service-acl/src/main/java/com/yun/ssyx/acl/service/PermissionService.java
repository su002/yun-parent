package com.yun.ssyx.acl.service;

import com.yun.ssyx.model.acl.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface PermissionService extends IService<Permission> {

    //查询所有菜单
    List<Permission> queryAllPermission();

    //递归删除菜单
    void removeChildById(Long id);

    List<Permission> queryAllPerByRoleId(Long roleId);

    void toAssign(Long id, Long[] ids);
}
