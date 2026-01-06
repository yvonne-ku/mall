package com.noinch.mall.biz.product.domain.mode;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Data
@Document(indexName = "product_index_test")
public class ProductIndex {

    /**
     * 文档唯一表示，通常用 skuId
     */
    @Id
    private String id;

    /**
     * 商品 SPU 唯一标识，用于展示时更精细的 “去重” 或 “聚合” 操作
     */
    @Field(type = FieldType.Keyword)
    private String spuId;

    /**
     * 商品子标题，spu 中对商品特质的描述
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String subTitle;

    /**
     * 商品属性，sku 中对商品的具体描述，如：颜色、尺寸、重量等
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String attribute;

    /**
     * 商品名称
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String name;

    /**
     * 商品品牌
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String brandName;

    /**
     * 商品分类
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String categoryName;

    /**
     * 商品价格
     */
    @Field(type = FieldType.Double)
    private Double price;
}
