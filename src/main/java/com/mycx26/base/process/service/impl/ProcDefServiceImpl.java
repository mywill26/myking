package com.mycx26.base.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mycx26.base.context.UserContext;
import com.mycx26.base.enump.Yn;
import com.mycx26.base.exception.DataException;
import com.mycx26.base.exception.ParamException;
import com.mycx26.base.process.constant.ProcCacheConstant;
import com.mycx26.base.process.entity.ProcDef;
import com.mycx26.base.process.mapper.ProcDefMapper;
import com.mycx26.base.process.service.ProcDefService;
import com.mycx26.base.process.service.query.DefQuery;
import com.mycx26.base.service.base.impl.BaseServiceImpl;
import com.mycx26.base.service.bo.SelectOption;
import com.mycx26.base.service.dto.PageData;
import com.mycx26.base.util.SpringUtil;
import com.mycx26.base.util.StringUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 流程定义 服务实现类
 * </p>
 *
 * @author mycx26
 * @since 2020-05-22
 */
@Service
public class ProcDefServiceImpl extends BaseServiceImpl<ProcDefMapper, ProcDef> implements ProcDefService {

    @Resource
    private ProcDefService procDefService;

    @PostConstruct
    private void init() {
        procDefService = SpringUtil.getBean(ProcDefServiceImpl.class);
    }

    @Override
    public PageData<ProcDef> getList(DefQuery defQuery) {
        String procDefKey = defQuery.getProcDefKey();
        String name = defQuery.getProcDefName();
        return new PageData<>(page(new Page<>(defQuery.getCurrent(), defQuery.getSize()),
                new QueryWrapper<ProcDef>()
                        .like(StringUtil.isNotBlank(procDefKey), "proc_def_key", procDefKey)
                        .like(StringUtil.isNotBlank(name), "proc_def_name", name)
                        .eq("is_internal", true)
                        .orderByDesc("create_time")
        ));
    }

    @Override
    public void add(ProcDef procDef) {
        addValidate(procDef);
        procDef.setCreatorId(UserContext.getUserId());

        try {
            save(procDef);
        } catch (DuplicateKeyException e) {
            throw new DataException("Data is exist");
        }
    }

    private void addValidate(ProcDef procDef) {
        StringBuilder sb = new StringBuilder();

        if (StringUtil.isBlank(procDef.getProcDefKey())) {
            StringUtil.append(sb, "Process definition key is required");
        }
        if (StringUtil.isBlank(procDef.getProcDefName())) {
            StringUtil.append(sb, "Process definition name is required");
        }
        if (StringUtil.isBlank(procDef.getMainForm())) {
            StringUtil.append(sb, "Main form is required");
        }

        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
            throw new ParamException(sb.toString());
        }
    }

    @Override
    public void modify(ProcDef procDef) {
        modifyValidate(procDef);
        update(Wrappers.<ProcDef>lambdaUpdate()
                .eq(ProcDef::getProcDefKey, procDef.getProcDefKey())
                .set(ProcDef::getProcDefName, procDef.getProcDefName())
                .set(ProcDef::getFlowNoPrefix, procDef.getFlowNoPrefix())
                .set(ProcDef::getCancel, procDef.getCancel())
                .set(ProcDef::getDescription, procDef.getDescription())
                .set(ProcDef::getOrderNo, procDef.getOrderNo())
                .set(ProcDef::getYn, procDef.getYn())
                .set(ProcDef::getModifierId, UserContext.getUserId())
        );
    }

    private void modifyValidate(ProcDef procDef) {
        StringBuilder sb = new StringBuilder();
        if (StringUtil.isBlank(procDef.getProcDefKey())) {
            StringUtil.append(sb, "Process definition key is required");
        }
        if (StringUtil.isBlank(procDef.getProcDefName())) {
            StringUtil.append(sb, "Process definition name is required");
        }
        if (StringUtil.isBlank(procDef.getFlowNoPrefix())) {
            StringUtil.append(sb, "Flow no prefix is required");
        }
        boolean flag = null == procDef.getCancel()
                || null == procDef.getOrderNo()
                || null == procDef.getYn();
        if (flag) {
            StringUtil.append(sb, "Cancel, order no, yn are required");
        }

        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
            throw new ParamException(sb.toString());
        }
    }

    @Override
    public List<SelectOption> getAll() {
        return list(new QueryWrapper<ProcDef>().eq("is_internal", true).eq("yn", Yn.YES.getCode()),
                e -> new SelectOption().setLabel(e.getProcDefName()).setCode(e.getProcDefKey()));
    }

    @Cacheable(value = ProcCacheConstant.PROC_DEF, key="#procDefKey",
            condition = "#procDefKey != null", unless = "null == #result")
    @Override
    public ProcDef getByKey(String procDefKey) {
        if (StringUtil.isBlank(procDefKey)) {
            return null;
        }

        return getOne(new QueryWrapper<ProcDef>().eq("proc_def_key", procDefKey).eq("yn", Yn.YES.getCode()));
    }

    @Override
    public String getNameByKey(String procDefKey) {
        ProcDef procDef = procDefService.getByKey(procDefKey);
        if (procDef != null) {
            return procDef.getProcDefName();
        }

        return null;
    }

    @Cacheable(value = ProcCacheConstant.PROC_DEF, key="#engineKey",
            condition = "#engineKey != null", unless = "null == #result")
    @Override
    public ProcDef getByEngineKey(String engineKey) {
        if (StringUtil.isBlank(engineKey)) {
            return null;
        }

        return getOne(new QueryWrapper<ProcDef>().eq("engine_key", engineKey).eq("yn", Yn.YES.getCode()));
    }

    @Cacheable(value = ProcCacheConstant.PROC_DEF, key="#engineId",
            condition = "#engineId != null", unless = "null == #result")
    @Override
    public ProcDef getByEngineId(String engineId) {
        if (StringUtil.isBlank(engineId)) {
            return null;
        }

        return getOne(new QueryWrapper<ProcDef>().eq("engine_id", engineId).eq("yn", Yn.YES.getCode()));
    }

    @Cacheable(value = ProcCacheConstant.PROC_DEF, key="#flowNoPrefix",
            condition = "#flowNoPrefix != null", unless = "null == #result")
    @Override
    public ProcDef getByFlowNoPrefix(String flowNoPrefix) {
        if (StringUtil.isBlank(flowNoPrefix)) {
            return null;
        }

        return getOne(Wrappers.<ProcDef>lambdaQuery().eq(ProcDef::getFlowNoPrefix, flowNoPrefix));
    }
}
