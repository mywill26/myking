package com.mycx26.base.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mycx26.base.process.entity.ProcRole;
import com.mycx26.base.process.service.query.ProcRoleQuery;

/**
 * <p>
 * 流程角色 Mapper 接口
 * </p>
 *
 * @author mycx26
 * @since 2020-06-28
 */
public interface ProcRoleMapper extends BaseMapper<ProcRole> {

    IPage<ProcRole> getList(IPage<ProcRole> page, ProcRoleQuery roleQuery);
}
