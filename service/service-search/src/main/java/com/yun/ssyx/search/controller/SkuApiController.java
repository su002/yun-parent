package com.yun.ssyx.search.controller;

import com.yun.ssyx.common.result.Result;
import com.yun.ssyx.model.search.SkuEs;
import com.yun.ssyx.search.service.SkuService;
import com.yun.ssyx.vo.search.SkuEsQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ES模块实现调用openfeign接口实现上架
 */

@RestController
@RequestMapping("/api/search/sku")
public class SkuApiController {

    @Autowired
    private SkuService skuService;

    //上架
    @GetMapping("inner/upperSku/{skuId}")
    public Result upSkuProduct(@PathVariable Long skuId) {
        skuService.upSkuProduct( skuId);
        return Result.ok(null);
    }

    //上架
    @GetMapping("inner/lowerSku/{skuId}")
    public Result lowerSkuProduct(@PathVariable Long skuId) {
        skuService.lowerSkuProduct( skuId);
        return Result.ok(null);

    }

    //获取火爆商品
    @GetMapping("inner/findHotSkuList")
    public List<SkuEs> getHotProduct() {
        List<SkuEs> skuInfoList = skuService.findHotSkuList();
        return skuInfoList;
    }

    //数据分类

    @GetMapping("inner/getCategoryList")
    public Result getCategoryList(@PathVariable Integer page ,
                                        SkuEsQueryVo skuEsQueryVo,
                                        @PathVariable Integer limit) {
        Pageable pageable =  PageRequest.of(page, limit);
        Page<SkuEs> pageModel =  skuService.findCategoryList(pageable , skuEsQueryVo);

        return Result.ok(pageModel);
    }


}
