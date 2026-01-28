

package com.noinch.mall.springboot.starter.mybatisplus;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.noinch.mall.springboot.starter.convention.page.PageRequest;
import com.noinch.mall.springboot.starter.convention.page.PageResponse;
import org.springframework.beans.BeanUtils;

/**
 * 分页工具类
 *
 */
public class PageUtil {
    
    /**
     * {@link PageRequest} to {@link Page}
     *
     * @param pageRequest
     * @return
     */
    public static Page convert(PageRequest pageRequest) {
        return convert(pageRequest.getCurrent(), pageRequest.getSize());
    }
    
    /**
     * {@link PageRequest} to {@link Page}
     *
     * @param current
     * @param size
     * @return
     */
    public static Page convert(long current, long size) {
        return new Page(current, size);
    }
    
    /**
     * {@link IPage} to {@link PageRequest}
     *
     * @param iPage
     * @return
     */
    public static PageResponse convert(IPage iPage) {
        return buildConventionPage(iPage);
    }
    
    /**
     * {@link IPage} to {@link PageRequest}
     *
     * @param iPage
     * @param targetClass
     * @param <TARGET>
     * @param <ORIGINAL>
     * @return
     */
    public static <TARGET, ORIGINAL> PageResponse<TARGET> convert(IPage<ORIGINAL> iPage, Class<TARGET> targetClass) {
        iPage.convert(each -> {
            try {
                Object target = targetClass.getDeclaredConstructor().newInstance();
                BeanUtil.copyProperties(each, target);
                return target;
            } catch (Exception e) {
                throw new RuntimeException("对象转换失败", e);
            }
        });
        return buildConventionPage(iPage);
    }
    
    /**
     * {@link IPage} build to {@link PageRequest}
     *
     * @param iPage
     * @return
     */
    private static PageResponse buildConventionPage(IPage iPage) {
        return PageResponse.builder().current(iPage.getCurrent()).size(iPage.getSize()).records(iPage.getRecords()).total(iPage.getTotal()).build();
    }
}
