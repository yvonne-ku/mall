

package com.noinch.mall.biz.bff.dto.resp.adapter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 商品返回包装适配返回
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeGoodsResultAdapterRespDTO {
    
    private Long total;
    
    private List<HomeGoodsAdapterRespDTO> data;
}
