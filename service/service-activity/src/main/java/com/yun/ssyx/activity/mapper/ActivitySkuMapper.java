package com.yun.ssyx.activity.mapper;

import com.yun.ssyx.model.activity.ActivitySku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 活动参与商品 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2023-04-07
 */
@Repository
public interface ActivitySkuMapper extends BaseMapper<ActivitySku> {

}
