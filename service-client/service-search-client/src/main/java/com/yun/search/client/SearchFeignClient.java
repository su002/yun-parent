package com.yun.search.client;

import com.yun.ssyx.model.search.SkuEs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


/**
 * 使用openfeign实现调用product的接口
 */

@FeignClient(value = "service-search")
public interface SearchFeignClient {
    @GetMapping("/api/search/sku/inner/findHotSkuList")
    List<SkuEs> getHotProduct() ;

}
