package com.mycx26.base.process.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mycx26.base.process.constant.ProcCacheConstant;
import com.mycx26.base.process.entity.CombineView;
import com.mycx26.base.process.mapper.CombineViewMapper;
import com.mycx26.base.process.service.CombineViewService;
import com.mycx26.base.util.StringUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 组合视图 服务实现类
 * </p>
 *
 * @author mycx26
 * @since 2022-07-24
 */
@Service
public class CombineViewServiceImpl extends ServiceImpl<CombineViewMapper, CombineView> implements CombineViewService {

    @Cacheable(value = ProcCacheConstant.COMBINE_VIEW, key="#viewKey1",
            condition = "#viewKey1 != null", unless = "null == #result || #result.isEmpty()")
    @Override
    public List<CombineView> getByViewKey1(String viewKey1) {
        if (StringUtil.isBlank(viewKey1)) {
            return Collections.emptyList();
        }

        return list(Wrappers.<CombineView>lambdaQuery()
                .eq(CombineView::getViewKey1, viewKey1).orderByAsc(CombineView::getOrderNo));
    }
}
