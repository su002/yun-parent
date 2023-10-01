package com.yun.ssyx.home.controller;

import com.yun.ssyx.common.auth.AuthContextHolder;
import com.yun.ssyx.common.result.Result;
import com.yun.ssyx.home.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping("/index")
    public Result index() {
        // 根据ThreadLocal工具类中的方法获取登录时存放的用户id
        Long userId = AuthContextHolder.getUserId();
        Map<String, Object> userData = homeService.getIndexData(userId);

        return Result.ok(userData);
    }

}
