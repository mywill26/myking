package com.mycx26.base.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mycx26.base.process.entity.ProcRelation;
import com.mycx26.base.process.mapper.ProcRelationMapper;
import com.mycx26.base.process.service.ProcRelationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 流程关联关系 服务实现类
 * </p>
 *
 * @author mycx26
 * @since 2020-06-19
 */
@Service
public class ProcRelationServiceImpl extends ServiceImpl<ProcRelationMapper, ProcRelation> implements ProcRelationService {

    @Override
    public List<ProcRelation> getByProcInstId1(String procInstId1) {
        return list(new QueryWrapper<ProcRelation>().eq("proc_inst_id1", procInstId1));
    }
}
