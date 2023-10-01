package com.yun.ssyx.acl.controller;

import com.yun.ssyx.acl.service.PermissionService;
import com.yun.ssyx.common.result.Result;
import com.yun.ssyx.model.acl.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/acl/permission")
@Api(tags = "菜单管理")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    //查询所有菜单
//    url: `${api_name}`,
//    method: 'get'
    @ApiOperation("查询所有菜单")
    @GetMapping
    public Result list() {
        List<Permission> list = permissionService.queryAllPermission();
        return Result.ok(list);
    }

    //添加菜单
//    url: `${api_name}/save`,
//    method: "post",
//    data: permission
    @ApiOperation("添加菜单")
    @PostMapping("save")
    public Result save(@RequestBody Permission permission) {
        permissionService.save(permission);
        return Result.ok(null);
    }

    //修改菜单
//    url: `${api_name}/update`,
//    method: "put",
//    data: permission
    @ApiOperation("修改菜单")
    @PutMapping("update")
    public Result update(@RequestBody Permission permission) {
        permissionService.updateById(permission);
        return Result.ok(null);
    }

    //递归删除菜单
//    url: `${api_name}/remove/${id}`,
//    method: "delete"
    @ApiOperation("递归删除菜单")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        permissionService.removeChildById(id);
        return Result.ok(null);
    }



    @ApiOperation("获取已经分配的权限菜单")
    @GetMapping("/toAssign/{roleId}")
    public Result toAssign(@PathVariable("roleId") Long roleId){
//        返回所有权限的名称与已分配的列表
        List<Permission> list =  permissionService.queryAllPerByRoleId(roleId);
        return Result.ok(list);
    }

    @ApiOperation("分配权限")
    @GetMapping("/doAssign")
    public Result toAssign(@RequestParam Long id,
                            @RequestParam Long[] ids
    ){
//        返回所有权限的名称与已分配的列表
        permissionService.toAssign(id ,ids);
        return Result.ok(true);
    }



}
