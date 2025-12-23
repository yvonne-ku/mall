
package com.noinch.mall.biz.bff.service;

import com.noinch.mall.biz.bff.dto.resp.adapter.HomeGoodsResultAdapterRespDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.HomePanelAdapterRespDTO;

import java.util.List;

/**
 * 商品接口层
 *
 */
public interface HomeService {
    
    /**
     * 查询商城首页板块数据
     *
     * @return 商城首页板块返回数据
     */
    List<HomePanelAdapterRespDTO> listHomePanel();

    /**
     * 查询商城首页全部商品数据
     * @param page  页码
     * @param size  每页数量
     * @param sort  排序字段
     * @param priceGt  价格区间开始
     * @param priceLte 价格区间结束
     * @return 商城首页全部商品返回数据
     */
    HomeGoodsResultAdapterRespDTO allGoods(Integer page, Integer size, Integer sort, Integer priceGt, Integer priceLte);
}
