package com.yun.activity.client;

import com.yun.ssyx.model.search.SkuEs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;


/**
 * 使用openfeign实现调用search的接口
 */

@FeignClient(value = "service-search")
public interface ActivityFeignClient {

    @GetMapping("/api/search/sku/inner/getCategoryList")
    Map<Long, List<String>> findActivity(List<Long> skuIds);
}
