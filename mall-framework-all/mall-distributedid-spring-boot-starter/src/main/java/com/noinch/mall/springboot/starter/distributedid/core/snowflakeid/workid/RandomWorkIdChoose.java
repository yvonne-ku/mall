

package com.noinch.mall.springboot.starter.distributedid.core.snowflakeid.workid;

import lombok.extern.slf4j.Slf4j;

/**
 * 使用随机数获取雪花 WorkId */
@Slf4j
public class RandomWorkIdChoose implements WorkIdChooser {
    
    @Override
    public WorkIdWrapper chooseWorkId() {
        int start = 0, end = 31;
        return new WorkIdWrapper(getRandom(start, end), getRandom(start, end));
    }

    @Override
    public String getName() {
        return "RandomWorkIdChooser";
    }

    private static long getRandom(int start, int end) {
        return (long) (Math.random() * (end - start + 1) + start);
    }
}
