package com.noinch.mall.biz.product.domain.mode;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Document(indexName = "product_index_test")
public class ProductIndex {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String productSpuId;

    @Field(type = FieldType.Keyword)
    private String productSn;

    @Field(type = FieldType.Keyword)
    private String categoryName;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String name;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String brandName;

    @Field(type = FieldType.Double)
    private BigDecimal price;

    @Field(type = FieldType.Double)
    private BigDecimal promotionPrice;

    @Field(type = FieldType.Date)
    private Date promotionStartTime;

    @Field(type = FieldType.Date)
    private Date promotionEndTime;
}
