package com.yun.ssyx.acl.service.impl;

import com.yun.ssyx.acl.mapper.PermissionMapper;
import com.yun.ssyx.acl.service.PermissionRoleService;
import com.yun.ssyx.acl.service.PermissionService;
import com.yun.ssyx.acl.utils.PermissionHelper;
import com.yun.ssyx.model.acl.Permission;
import com.yun.ssyx.model.acl.RolePermission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    @Autowired
    PermissionRoleService roleService;


    //查询所有菜单
    @Override
    public List<Permission> queryAllPermission() {
        //1 查询所有菜单
        List<Permission> allPermissionList =
                baseMapper.selectList(null);

        //2 转换要求数据格式
        List<Permission> result = PermissionHelper.buildPermission(allPermissionList);
        return result;
    }

    //递归删除菜单
    @Override
    public void removeChildById(Long id) {
        //1 创建list集合idList，封装所有要删除菜单id
        List<Long> idList = new ArrayList<>();


        //根据当前菜单id，获取当前菜单下面所有子菜单，
        //如果子菜单下面还有子菜单，都要获取到
        //重点：递归找当前菜单子菜单
        this.getAllPermissionId(id, idList);

        //设置当前菜单id
        idList.add(id);

        //调用方法根据多个菜单id删除
        baseMapper.deleteBatchIds(idList);
    }


    //    获取已分配的权限与角色的信息
    @Override
    public List<Permission> queryAllPerByRoleId(Long roleId) {
//        查询所有权限
        List<Permission> permissionList = baseMapper.selectList(null);
        //根据角色列表查询角色拥有的权限
        LambdaQueryWrapper<RolePermission> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(RolePermission::getRoleId, roleId);
        // 获取所以permission的信息
        List<RolePermission> list = roleService.list(lambdaQueryWrapper);

        //获取所有权限已分配的菜单的全部id
        List<Long> ids = list.stream().map(
                item -> item.getPermissionId()
        ).collect(Collectors.toList());

        List<Permission> assignPermissionList = new ArrayList<>();
        for (Permission per : permissionList) {
            if (ids.contains(per.getId())) {
                assignPermissionList.add(per);
            }
        }

//                封装已分配的角色的菜单
        List<Permission> perList = new ArrayList<>();

        List<Permission> BulipermissionListRes = PermissionHelper.buildPermission(permissionList);
        BulipermissionListRes.forEach(item -> {
            System.out.println("11111" + item);
            //  判断是否为顶层
            perList.add(item);
            if (item.getPid() == 0) {
                if (ids.contains(item.getId())) {
                    item.setSelect(true);
                }
            }
            this.setAllPermissionId(item.getChildren(), ids);
        });

        //封装到map，返回
        Map<String, Object> result = new HashMap<>();
        //所有角色列表
        result.put("allRolesList", ids);
        //用户分配角色列表
        result.put("assignRoles", perList);
        return perList;
    }

    //    递归查找
    private void setAllPermissionId(List<Permission> children, List<Long> ids) {
        children.forEach(item ->{
            if (ids.contains(item.getPid())){
                item.setSelect(true);
            }
            this.setAllPermissionId(item.getChildren(),ids);
        });
    }







    //重点：递归找当前菜单下面的所有子菜单
    //第一个参数是当前菜单id
    //第二个参数最终封装list集合，包含所有菜单id
    private void getAllPermissionId(Long id, List<Long> idList) {
        //根据当前菜单id查询下面子菜单
        //select * from permission where pid=2
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getPid, id);
        List<Permission> childList = baseMapper.selectList(wrapper);

        //递归查询是否还有子菜单，有继续递归查询
        childList.stream().forEach(item -> {
            //封装菜单id到idList集合里面
            idList.add(item.getId());
            //递归
            this.getAllPermissionId(item.getId(), idList);
        });
    }


    /**
     *
     * 为角色分配菜单
     * @param id
     * @param ids
     */
    @Override
    public void toAssign(Long id, Long[] ids) {
//      //删除已分配
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getPermissionId,id);
        roleService.remove(wrapper);

        //从新分配
        List<RolePermission> arrayList = new ArrayList<>();
        for (Long aLong : ids) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(id);
            rolePermission.setPermissionId(aLong);
            arrayList.add(rolePermission)    ;
        }
        roleService.saveBatch(arrayList);
    }
}
