package com.yun.ssyx.search.repository;

import com.yun.ssyx.model.search.SkuEs;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SkuRepository extends ElasticsearchRepository<SkuEs,Long> {


    Page<SkuEs> findByOrderByHotScoreDesc(Pageable pageOf);

    Page<SkuEs> findByKeywordAndWareId(Pageable pageable, @Param("wareId") Long wareId, @Param("keyword") String keyword);

    Page<SkuEs> findByCategoryIdAndWareId(Pageable pageable, @Param("categoryId") Long categoryId, @Param("wareId") Long wareId);

}
