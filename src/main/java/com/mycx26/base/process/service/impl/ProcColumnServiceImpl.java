package com.mycx26.base.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mycx26.base.enump.Yn;
import com.mycx26.base.process.constant.ProcCacheConstant;
import com.mycx26.base.process.entity.ProcColumn;
import com.mycx26.base.process.mapper.ProcColumnMapper;
import com.mycx26.base.process.service.ProcColumnService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 流程列配置 服务实现类
 * </p>
 *
 * @author mycx26
 * @since 2020-06-21
 */
@Service
public class ProcColumnServiceImpl extends ServiceImpl<ProcColumnMapper, ProcColumn> implements ProcColumnService {

    @Cacheable(value = ProcCacheConstant.PROC_COL, key="#tblName",
            condition = "#tblName != null", unless = "null == #result || #result.isEmpty()")
    @Override
    public List<ProcColumn> getByTblName(String tblName) {
        return list(new QueryWrapper<ProcColumn>().eq("tbl_name", tblName).eq("yn", Yn.YES.getCode()));
    }

    @Override
    public List<ProcColumn> getAddsByTblName(String tblName) {
        return list(new QueryWrapper<ProcColumn>().eq("tbl_name", tblName).eq("is_added", Yn.YES.getCode()).eq("yn", Yn.YES.getCode()));
    }
}
