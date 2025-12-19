
package com.noinch.mall.biz.bff.service;

import com.noinch.mall.biz.bff.dto.resp.adapter.HomePanelAdapterRespDTO;

import java.util.List;

/**
 * 商品接口层
 *
 */
public interface GoodsService {
    
    /**
     * 查询商城首页板块数据
     *
     * @return 商城首页板块返回数据
     */
    List<HomePanelAdapterRespDTO> listHomePanel();
    
}
