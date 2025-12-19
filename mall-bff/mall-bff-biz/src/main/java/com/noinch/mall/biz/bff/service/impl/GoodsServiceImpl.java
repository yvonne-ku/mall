package com.noinch.mall.biz.bff.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.noinch.mall.biz.bff.dao.entity.PanelDO;
import com.noinch.mall.biz.bff.dao.mapper.PanelMapper;
import com.noinch.mall.biz.bff.dto.resp.adapter.HomePanelAdapterRespDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.HomePanelContentAdapterRespDTO;
import com.noinch.mall.biz.bff.remote.ProductRemoteService;
import com.noinch.mall.biz.bff.remote.resp.ProductRespDTO;
import com.noinch.mall.biz.bff.service.GoodsService;
import com.noinch.mall.springboot.starter.common.toolkit.BeanUtil;
import com.noinch.mall.springboot.starter.convention.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsServiceImpl implements GoodsService {

    private final PanelMapper panelMapper;
    private final PanelProductRelationMapper panelProductRelationMapper;
    private final ProductRemoteService productRemoteService;

    private static final List<String> TYPE_TWO_LIST = Lists.newArrayList("1647777981810081792", "1647788115844136960", "1647794693754322944");


    @Override
    @Cached(name = "goods:", key = "'home-panel'", expire = 24, timeUnit = TimeUnit.HOURS)
    public List<HomePanelAdapterRespDTO> listHomePanel() {
        List<PanelDO> listAllPanel = panelMapper.listAllPanel();
        List<HomePanelAdapterRespDTO> result = BeanUtil.convert(listAllPanel, HomePanelAdapterRespDTO.class);
        result.forEach(each -> {
            List<PanelProductRelationDO> panelProductRelationList = panelProductRelationMapper.listPanelProductRelationByPanelId(each.getId());
            if (CollUtil.isNotEmpty(panelProductRelationList)) {
                List<HomePanelContentAdapterRespDTO> panelContents = new ArrayList<>();
                panelProductRelationList.forEach(item -> {
                    Result<ProductRespDTO> productResult = productRemoteService.getProductBySpuId(String.valueOf(item.getProductId()));
                    if (productResult.isSuccess() && productResult.getData() != null) {
                        HomePanelContentAdapterRespDTO productRespDTO = new HomePanelContentAdapterRespDTO();
                        ProductRespDTO resultData = productResult.getData();
                        ProductSpuRespDTO productSpu = resultData.getProductSpu();
                        productRespDTO.setProductName(productSpu.getName());
                        productRespDTO.setProductId(String.valueOf(productSpu.getId()));
                        productRespDTO.setSalePrice(productSpu.getPrice().intValue());
                        productRespDTO.setId(String.valueOf(productSpu.getId()));
                        productRespDTO.setSortOrder(item.getSort());
                        productRespDTO.setSubTitle(productSpu.getSubTitle());
                        productRespDTO.setPanelId(each.getId());
                        productRespDTO.setType(0);
                        productRespDTO.setCreated(new Date());
                        productRespDTO.setUpdated(new Date());
                        List<String> pics = StrUtil.split(Optional.ofNullable(item.getPic()).orElse(productSpu.getPic()), ",");
                        if (CollUtil.isNotEmpty(pics)) {
                            productRespDTO.setProductImageBig(Optional.ofNullable(item.getBigPic()).orElse(pics.get(0)));
                            productRespDTO.setPicUrl(pics.get(0));
                            if (pics.size() > 1) {
                                productRespDTO.setPicUrl2(pics.get(1));
                            }
                            if (pics.size() > 2) {
                                productRespDTO.setPicUrl3(pics.get(2));
                            }
                        }
                        if (TYPE_TWO_LIST.contains(String.valueOf(productSpu.getId()))) {
                            productRespDTO.setType(2);
                        }
                        panelContents.add(productRespDTO);
                    }
                });
                each.setPanelContents(panelContents);
            }
        });
        return result;
    }

}
