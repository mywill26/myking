package com.mycx26.base.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mycx26.base.enump.Yn;
import com.mycx26.base.process.constant.ProcCacheConstant;
import com.mycx26.base.process.entity.ProcViewCol;
import com.mycx26.base.process.mapper.ProcViewColMapper;
import com.mycx26.base.process.service.ProcViewColService;
import com.mycx26.base.service.base.impl.BaseServiceImpl;
import com.mycx26.base.util.StringUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 流程视图列配置 服务实现类
 * </p>
 *
 * @author mycx26
 * @since 2020-07-15
 */
@Service
public class ProcViewColServiceImpl extends BaseServiceImpl<ProcViewColMapper, ProcViewCol> implements ProcViewColService {

    @Cacheable(value = ProcCacheConstant.VIEW_COL, key="#viewKey", unless = "null == #result || #result.isEmpty()")
    @Override
    public List<ProcViewCol> getByViewKey(String viewKey) {
        if (StringUtil.isBlank(viewKey)) {
            return Collections.emptyList();
        }

        return list(new QueryWrapper<ProcViewCol>().eq("view_key", viewKey).eq("yn", Yn.YES.getCode()));
    }
}
