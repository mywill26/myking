package com.mycx26.base.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mycx26.base.process.entity.CombineView;

import java.util.List;

/**
 * <p>
 * 组合视图 服务类
 * </p>
 *
 * @author mycx26
 * @since 2022-07-24
 */
public interface CombineViewService extends IService<CombineView> {

    /**
     * get by view key1
     *
     * @param viewKey1 view key1
     * @return combine views
     */
    List<CombineView> getByViewKey1(String viewKey1);
}
