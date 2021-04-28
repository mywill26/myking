package com.mycx26.base.process.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mycx26.base.enump.Yn;
import com.mycx26.base.exception.ParamException;
import com.mycx26.base.process.entity.ProcRole;
import com.mycx26.base.process.mapper.ProcRoleMapper;
import com.mycx26.base.process.service.ProcRoleService;
import com.mycx26.base.process.service.query.ProcRoleQuery;
import com.mycx26.base.service.dto.PageData;
import com.mycx26.base.util.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 流程角色 服务实现类
 * </p>
 *
 * @author mycx26
 * @since 2020-06-28
 */
@Service
public class ProcRoleServiceImpl extends ServiceImpl<ProcRoleMapper, ProcRole> implements ProcRoleService {

    @Resource
    private ProcRoleMapper procRoleMapper;

    @Override
    public PageData<ProcRole> getList(ProcRoleQuery roleQuery) {
        return new PageData<>(procRoleMapper.getList(new Page<>(roleQuery.getCurrent(), roleQuery.getSize()), roleQuery));
    }

    @Override
    public void modify(ProcRole procRole) {
        if (StringUtil.isAnyBlank(procRole.getRoleCode(), procRole.getRoleName())) {
            throw new ParamException("Process role code and name are required");
        }

        update(Wrappers.<ProcRole>lambdaUpdate()
                .set(ProcRole::getRoleName, procRole.getRoleName())
                .set(ProcRole::getDescription, procRole.getDescription())
                .eq(ProcRole::getRoleCode, procRole.getRoleCode())
        );
    }

    @Override
    public ProcRole getByRoleCode(String roleCode) {
        if (StringUtil.isBlank(roleCode)) {
            return null;
        }

        return getOne(Wrappers.<ProcRole>lambdaQuery().eq(ProcRole::getRoleCode, roleCode).eq(ProcRole::getYn, Yn.YES.getCode()));
    }

    @Override
    public String getRoleNameByCode(String roleCode) {
        ProcRole procRole = getByRoleCode(roleCode);
        if (procRole != null) {
            return procRole.getRoleName();
        }

        return null;
    }
}
