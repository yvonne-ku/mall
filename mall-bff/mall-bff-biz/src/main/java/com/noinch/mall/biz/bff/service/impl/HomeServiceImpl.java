package com.noinch.mall.biz.bff.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alicp.jetcache.anno.Cached;
import com.google.common.collect.Lists;
import com.noinch.mall.biz.bff.dao.entity.PanelDO;
import com.noinch.mall.biz.bff.dao.entity.PanelProductRelationDO;
import com.noinch.mall.biz.bff.dao.mapper.PanelMapper;
import com.noinch.mall.biz.bff.dao.mapper.PanelProductRelationMapper;
import com.noinch.mall.biz.bff.dto.resp.adapter.HomeGoodsAdapterRespDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.HomeGoodsResultAdapterRespDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.HomePanelAdapterRespDTO;
import com.noinch.mall.biz.bff.dto.resp.adapter.HomePanelContentAdapterRespDTO;
import com.noinch.mall.biz.bff.remote.ProductRemoteService;
import com.noinch.mall.biz.bff.remote.resp.ProductRespDTO;
import com.noinch.mall.biz.bff.remote.resp.ProductSpuRespDTO;
import com.noinch.mall.biz.bff.service.HomeService;
import com.noinch.mall.springboot.starter.common.toolkit.BeanUtil;
import com.noinch.mall.springboot.starter.convention.exception.ServiceException;
import com.noinch.mall.springboot.starter.convention.page.PageResponse;
import com.noinch.mall.springboot.starter.convention.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final PanelMapper panelMapper;
    private final PanelProductRelationMapper panelProductRelationMapper;
    private final ProductRemoteService productRemoteService;

    // 放大显示的图片
    private static final List<String> TYPE_TWO_LIST = Lists.newArrayList("1647777981810081792", "1647788115844136960", "1647794693754322944");


    @Override
//    @Cached(name = "home:", key = "panel", expire = 24, timeUnit = TimeUnit.HOURS)
    public List<HomePanelAdapterRespDTO> listHomePanel() {
        List<PanelDO> listAllPanel = panelMapper.listAllPanel();
        List<HomePanelAdapterRespDTO> result = BeanUtil.convert(listAllPanel, HomePanelAdapterRespDTO.class);
        result.forEach(each -> {
            // 获得当前板块下的所有商品关联记录
            List<PanelProductRelationDO> panelProductRelationList = panelProductRelationMapper.listPanelProductRelationByPanelId(each.getId());
            if (CollUtil.isNotEmpty(panelProductRelationList)) {
                List<HomePanelContentAdapterRespDTO> panelContents = new ArrayList<>();
                panelProductRelationList.forEach(item -> {
                    // 通过商品 ID 获得商品详情
                    Result<ProductRespDTO> productResult = productRemoteService.getProductBySpuId(String.valueOf(item.getProductId()));
                    if (productResult.isSuccess() && productResult.getData() != null) {
                        HomePanelContentAdapterRespDTO productRespDTO = new HomePanelContentAdapterRespDTO();
                        // 补充商品 spu 信息
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
                        // 补充商品图片信息（但优先使用关联记录中的图片信息，活动板块的图片在关联商品中，不在 spu 中，而且 spu 中有活动的特殊 id）
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

    @Override
//    @Cached(name = "home:", key = "'all-goods-page-'+#page+'-'+#size+'-'+#sort+'-'+#priceGt+'-'+#priceLte", expire = 24, timeUnit = TimeUnit.HOURS)
    public HomeGoodsResultAdapterRespDTO allGoods(Integer page, Integer size, Integer sort, Integer priceGt, Integer priceLte) {
        Result<PageResponse<ProductRespDTO>> pageResponseResult = null;
        try {
            pageResponseResult = productRemoteService.pageQueryProduct(page, size, sort, priceGt, priceLte);
            if (!pageResponseResult.isSuccess() || pageResponseResult.getData() == null) {
                throw new ServiceException("调用商品服务分页查询商品失败");
            }
        } catch (Throwable ex) {
            log.error("调用商品服务分页查询商品失败", ex);
        }
        PageResponse<ProductRespDTO> pageResponse = pageResponseResult.getData();
        List<ProductRespDTO> records = pageResponse.getRecords();
        List<HomeGoodsAdapterRespDTO> goodsAdapter = new ArrayList<>();
        records.stream().map(ProductRespDTO::getProductSpu).forEach(each -> {
            HomeGoodsAdapterRespDTO item = new HomeGoodsAdapterRespDTO();
            item.setProductId(String.valueOf(each.getId()));
            item.setProductName(each.getName());
            item.setSubTitle(each.getSubTitle());
            item.setSalePrice(each.getPrice().intValue());
            item.setProductImageBig(each.getPic());
            goodsAdapter.add(item);
        });
        return new HomeGoodsResultAdapterRespDTO(pageResponse.getTotal(), goodsAdapter);
    }

}
