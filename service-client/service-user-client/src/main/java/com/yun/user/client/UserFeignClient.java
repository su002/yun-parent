package com.yun.user.client;

import com.yun.ssyx.vo.user.LeaderAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


/**
 * 使用openfeign实现调用product的接口
 */

@FeignClient(value = "service-product")
public interface UserFeignClient {
    //获取用户信息
    @GetMapping("/api/user/leader/inner/getUserAddressByUserId/{userId}")
    public LeaderAddressVo getUserAddressByUserId(@PathVariable("userId") Long userId) ;


}
