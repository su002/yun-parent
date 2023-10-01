package com.yun.ssyx.home.service.impl;

import com.yun.activity.client.ActivityFeignClient;
import com.yun.client.product.ProductFeignClient;
import com.yun.search.client.SearchFeignClient;
import com.yun.ssyx.common.result.Result;
import com.yun.ssyx.home.service.HomeService;
import com.yun.ssyx.model.product.Category;
import com.yun.ssyx.model.product.SkuInfo;
import com.yun.ssyx.model.search.SkuEs;
import com.yun.ssyx.vo.user.LeaderAddressVo;
import com.yun.user.client.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class HomeServiceImpl implements HomeService {
    @Autowired(required = false)
    private UserFeignClient userFeignClient;
    @Autowired(required = false)
    private ProductFeignClient productFeignClient;
    @Autowired(required = false)
    private SearchFeignClient searchFeignClient;
    @Autowired(required = false)
    private ActivityFeignClient activityFeignClient;
    @Override
    public Map<String, Object> getIndexData(Long userId) {
        Map<String, Object> resMap = new HashMap<>();

        //1: 根据id获取当前登录用户的收获地址信息
        // 远程调用service-user 获取信息
        LeaderAddressVo userAddressByUserId = userFeignClient.getUserAddressByUserId(userId);
        resMap.put("userAddressByUserId", userAddressByUserId);
        //3: 获取所有分类 调用service-product
        List<Category> allCategoryList = productFeignClient.findAllCategoryList();
        resMap.put("allCategoryList", allCategoryList);
        //4: 获取新用户的专享商品
            //1 sku-info 表查询is_new_person 字段
            //2 sku-info 表查询publish_status
        List<SkuInfo> newPersonSkuInfoList = productFeignClient.findNewPersonSkuInfoList();
        resMap.put("newPersonSkuInfoList", newPersonSkuInfoList);
        //5: 获取销售火爆商品
        List<SkuEs> hotProduct = searchFeignClient.getHotProduct();
        resMap.put("hotProduct", hotProduct);
        //6: 获取sku对应的促销活动标签
        if (!hotProduct.isEmpty()) {
            // 获取所有商品的id
            List<Long> skuIds = hotProduct.stream().map(sku -> sku.getId()).collect(Collectors.toList());
            Map<Long, List<String>> skuIdToRuleListMap = activityFeignClient.findActivity(skuIds);


        }

        return resMap;
    }
}
