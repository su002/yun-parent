package com.yun.ssyx.search.service;

import com.yun.ssyx.model.search.SkuEs;
import com.yun.ssyx.vo.search.SkuEsQueryVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SkuService {
    void upSkuProduct(Long skuId);

    void lowerSkuProduct(Long skuId);

    List<SkuEs> findHotSkuList();

    Page<SkuEs> findCategoryList(Pageable pageable, SkuEsQueryVo skuEsQueryVo);
}
