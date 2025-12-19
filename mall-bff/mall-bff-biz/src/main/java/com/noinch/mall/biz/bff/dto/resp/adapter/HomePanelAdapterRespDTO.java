package com.noinch.mall.biz.bff.dto.resp.adapter;

import java.util.Date;
import java.util.List;

public class HomePanelAdapterRespDTO {

    /**
     * ID
     */
    private Integer id;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 限制数量
     */
    private Integer limitNum;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 修改时间
     */
    private Date updated;

    /**
     * 商品数组
     */
    private List<HomePanelContentAdapterRespDTO> panelContents;
}
