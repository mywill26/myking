package com.mycx26.base.process.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mycx26.base.enump.Yn;
import com.mycx26.base.process.constant.ProcCacheConstant;
import com.mycx26.base.process.entity.ProcFormView;
import com.mycx26.base.process.mapper.ProcFormViewMapper;
import com.mycx26.base.process.service.ProcFormViewService;
import com.mycx26.base.util.StringUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 流程表单视图 服务实现类
 * </p>
 *
 * @author mycx26
 * @since 2021-05-17
 */
@Service
public class ProcFormViewServiceImpl extends ServiceImpl<ProcFormViewMapper, ProcFormView> implements ProcFormViewService {

    @Cacheable(value = ProcCacheConstant.FORM_VIEW, key="#viewKey",
            condition = "#viewKey != null", unless = "null == #result")
    @Override
    public ProcFormView getByViewKey(String viewKey) {
        if (StringUtil.isBlank(viewKey)) {
            return null;
        }

        return getOne(Wrappers.<ProcFormView>lambdaQuery()
                .eq(ProcFormView::getViewKey, viewKey).eq(ProcFormView::getYn, Yn.YES.getCode()));
    }
}
