package com.yun.ssyx.search.service.Impl;

import com.yun.activity.client.ActivityFeignClient;
import com.yun.client.product.ProductFeignClient;
import com.yun.ssyx.common.auth.AuthContextHolder;
import com.yun.ssyx.enums.SkuType;
import com.yun.ssyx.model.product.Category;
import com.yun.ssyx.model.product.SkuInfo;
import com.yun.ssyx.model.search.SkuEs;
import com.yun.ssyx.search.repository.SkuRepository;
import com.yun.ssyx.search.service.SkuService;
import com.yun.ssyx.vo.search.SkuEsQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    private SkuRepository skuRepository;

    @Autowired(required = false)
    private ProductFeignClient productFeignClient;
    @Autowired(required = false)
    private ActivityFeignClient activityFeignClient;
    @Autowired(required = false)
    private AuthContextHolder authContextHolder;

    //    上架
    @Override
    public void upSkuProduct(Long skuId) {
        //通过远程调用,根据id获取所有信息
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        if (skuInfo == null) {
            return;
        }
        Category category = productFeignClient.getCategory(skuInfo.getCategoryId());
        // 获取数据并封装
        SkuEs skuEs = new SkuEs();
        if (category != null) {
            skuEs.setCategoryId(category.getId());
            skuEs.setCategoryName(category.getName());
        }
        skuEs.setId(skuInfo.getId());
        skuEs.setKeyword(skuInfo.getSkuName() + "," + skuEs.getCategoryName());
        skuEs.setWareId(skuInfo.getWareId());
        skuEs.setIsNewPerson(skuInfo.getIsNewPerson());
        skuEs.setImgUrl(skuInfo.getImgUrl());
        skuEs.setTitle(skuInfo.getSkuName());
        if (skuInfo.getSkuType() == SkuType.COMMON.getCode()) {  //普通商品
            skuEs.setSkuType(0);
            skuEs.setPrice(skuInfo.getPrice().doubleValue());
            skuEs.setStock(skuInfo.getStock());
            skuEs.setSale(skuInfo.getSale());
            skuEs.setPerLimit(skuInfo.getPerLimit());
        }

        //调用方法添加到ES
        SkuEs resSku = skuRepository.save(skuEs);
        if (resSku == null) {
            return;
        }

    }

    // 获取火爆商品
    @Override
    public List<SkuEs> findHotSkuList() {
        //获取分页数据 ,从0页开始
        Pageable pageOf = PageRequest.of(0, 10);
        Page<SkuEs> skuEsPage = skuRepository.findByOrderByHotScoreDesc(pageOf);
        // 获取销量前几的商品
        List<SkuEs> skuEsList = skuEsPage.getContent();
        return skuEsList;
    }

    //    下架
    @Override
    public void lowerSkuProduct(Long skuId) {
        //根据id下架
        skuRepository.deleteById(skuId);
    }

    //获取数据分类
    @Override
    public Page<SkuEs> findCategoryList(Pageable pageable, SkuEsQueryVo skuEsQueryVo) {
        // 1 获取登录用户所属于的团与收获地址的仓库id
        skuEsQueryVo.setWareId(AuthContextHolder.getWareId());

        // 2 根据条件查询

        // 使用springData 工具
        String keyword = skuEsQueryVo.getKeyword();
        Page<SkuEs> pageSku = null;
        // keyword 为null 则根据categoryId 与 wareId
        if (keyword != null && keyword.length() > 0) {
            pageSku = skuRepository.findByCategoryIdAndWareId(pageable,
                    skuEsQueryVo.getCategoryId(),
                    skuEsQueryVo.getWareId());

        } else {
            // keyword 不为null 根据三者查询
            pageSku = skuRepository.findByKeywordAndWareId(pageable,
                    skuEsQueryVo.getWareId(),
                    skuEsQueryVo.getKeyword());
        }

        // 3 查询商品的优惠活动
        List<SkuEs> skuEsList = pageSku.getContent();
        List<Long> skuIds = null;
        if (skuEsList != null && skuEsList.size() > 0) {
            skuIds = skuEsList.stream().map(item -> item.getId()
            ).collect(Collectors.toList());
        }

        // 根据所查询id 远程调用activity-client模块查询优惠活动
        Map<Long, List<String>> activityMap = activityFeignClient.findActivity(skuIds);
        if (activityMap!= null && activityMap.size() > 0) {
            skuEsList.forEach(item -> {
                List<String> activityList = activityMap.get(item.getId());
                // 将优惠规则封装
                item.setRuleList(activityList);
            });

        }

        return pageSku;
    }
}
