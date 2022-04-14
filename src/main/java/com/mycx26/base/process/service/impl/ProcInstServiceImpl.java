package com.mycx26.base.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mycx26.base.enump.Yn;
import com.mycx26.base.exception.DataException;
import com.mycx26.base.exception.ParamException;
import com.mycx26.base.process.constant.ProcConstant;
import com.mycx26.base.process.entity.ProcInst;
import com.mycx26.base.process.enump.InstanceStatus;
import com.mycx26.base.process.mapper.ProcInstMapper;
import com.mycx26.base.process.service.ProcInstService;
import com.mycx26.base.process.service.query.InstQuery;
import com.mycx26.base.service.EnumValueService;
import com.mycx26.base.service.ExternalUserService;
import com.mycx26.base.service.bo.ContextUser;
import com.mycx26.base.service.dto.PageData;
import com.mycx26.base.util.CollectionUtil;
import com.mycx26.base.util.SqlUtil;
import com.mycx26.base.util.StringUtil;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 流程实例 服务实现类
 * </p>
 *
 * @author mycx26
 * @since 2020-05-22
 */
@Service
public class ProcInstServiceImpl extends ServiceImpl<ProcInstMapper, ProcInst> implements ProcInstService {

    @Resource
    private EnumValueService enumValueService;

    @Resource
    private ExternalUserService externalUserService;

    @Override
    public PageData<ProcInst> getList(InstQuery instQuery) {
        IPage<ProcInst> page = page(new Page<>(instQuery.getCurrent(), instQuery.getSize()),
                new QueryWrapper<ProcInst>()
                        .eq(StringUtil.isNotBlank(instQuery.getProcDefKey()), "proc_def_key", instQuery.getProcDefKey())
                        .eq(StringUtil.isNotBlank(instQuery.getProcInstId()), "proc_inst_id", instQuery.getProcInstId())
                        .like(StringUtil.isNotBlank(instQuery.getProcInstName()), "proc_inst_name", instQuery.getProcInstName())
                        .eq(StringUtil.isNotBlank(instQuery.getFlowNo()), "flow_no", instQuery.getFlowNo())
                        .eq(StringUtil.isNotBlank(instQuery.getStatusCode()), "status_code", instQuery.getStatusCode())
                        .like(StringUtil.isNotBlank(instQuery.getCreatorId()), "creator_id", instQuery.getCreatorId())
                        .eq("yn", Yn.YES.getCode())
                        .orderByDesc("create_time")
        );

        if (!page.getRecords().isEmpty()) {
            page.getRecords().forEach(e -> e.setProcInstStatus(enumValueService
                    .getNameByTypeAndValueCode(ProcConstant.PROC_INST_STATUS, e.getStatusCode())));
        }

        return new PageData<>(page);
    }

    @Override
    public void add(ProcInst procInst) {
        addValidate(procInst);

        try {
            save(procInst);
        } catch (DuplicateKeyException e) {
            throw new DataException("Data is exist");
        }
    }

    private void addValidate(ProcInst procInst) {
        StringBuilder sb = new StringBuilder();

        if (StringUtil.isBlank(procInst.getProcDefKey())) {
            StringUtil.append(sb, "Process definition key is required");
        }
        if (StringUtil.isBlank(procInst.getFlowNo())) {
            StringUtil.append(sb, "Flow no is required");
        }
        if (StringUtil.isBlank(procInst.getStatusCode())) {
            StringUtil.append(sb, "Status code is required");
        }

        if (StringUtil.isBlank(procInst.getCreatorId())) {
            StringUtil.append(sb, "Creator id is required");
        } else {
            ContextUser user = externalUserService.getByUserId(procInst.getCreatorId());
            if (null == user) {
                StringUtil.append(sb, "Creator id invalid");
            } else {
                procInst.setCreator(user.getUsername())
                        .setCreatorEmail(user.getEmail())
                        .setCreatorDeptCode(user.getDeptCode())
                        .setCreatorDept(user.getDeptFullName());
            }
        }

        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
            throw new ParamException(sb.toString());
        }
    }

    @Override
    public ProcInst getByProcInstId(String procInstId) {
        if (StringUtil.isBlank(procInstId)) {
            return null;
        }

        return getOne(new QueryWrapper<ProcInst>().eq("proc_inst_id", procInstId));
    }

    @Override
    public List<ProcInst> getByProcInstIds(List<String> procInstIds) {
        if (CollectionUtil.isEmpty(procInstIds)) {
            return Collections.emptyList();
        }

        return list(Wrappers.<ProcInst>lambdaQuery().in(ProcInst::getProcInstId, procInstIds));
    }

    @Override
    public ProcInst getByFlowNo(String flowNo) {
        if (StringUtil.isBlank(flowNo)) {
            return null;
        }

        return getOne(new QueryWrapper<ProcInst>().eq("flow_no", flowNo));
    }

    @Override
    public List<ProcInst> getByFlowNos(List<String> flowNos) {
        if (CollectionUtil.isEmpty(flowNos)) {
            return Collections.emptyList();
        }

        return list(Wrappers.<ProcInst>lambdaQuery().in(ProcInst::getFlowNo, flowNos));
    }

    @Override
    public ProcInst end(String procInstId) {
        ProcInst procInst = getByProcInstId(procInstId);
        if (null == procInst) {
            throw new ParamException("Process instance not exist");
        }

        String col = SqlUtil.camelToUnderline(ProcConstant.PROC_INST_STATUS_CODE);
        boolean flag = update(new UpdateWrapper<ProcInst>()
                .set(col, InstanceStatus.END.getCode())
                .set(SqlUtil.camelToUnderline(ProcConstant.END_TIME), new Date())
                .eq(SqlUtil.camelToUnderline(ProcConstant.PROC_INST_ID), procInstId)
                .eq(col, InstanceStatus.RUN.getCode())
        );

        if (!flag) {
            throw new DataException("Process has end");
        }

        return procInst;
    }

    @Override
    public ProcInst rejectFirst(String procInstId) {
        ProcInst procInst = getByProcInstId(procInstId);
        if (null == procInst) {
            throw new ParamException("Process instance not exist");
        }

        boolean flag = update(Wrappers.<ProcInst>lambdaUpdate()
                .set(ProcInst::getStatusCode, InstanceStatus.REJECT_FIRST.getCode())
                .eq(ProcInst::getProcInstId, procInstId)
                .eq(ProcInst::getStatusCode, InstanceStatus.RUN.getCode())
        );

        if (!flag) {
            throw new DataException("Process has reject first");
        }

        return procInst;
    }

    @Override
    public ProcInst cancel(String procInstId) {
        ProcInst procInst = getByProcInstId(procInstId);
        if (null == procInst) {
            throw new ParamException("Process instance not exist");
        }

        boolean flag = update(Wrappers.<ProcInst>lambdaUpdate()
                .set(ProcInst::getStatusCode, InstanceStatus.CANCELED.getCode())
                .eq(ProcInst::getProcInstId, procInstId)
                .eq(ProcInst::getStatusCode, InstanceStatus.RUN.getCode())
        );

        if (!flag) {
            throw new DataException("Process has cancel");
        }

        return procInst;
    }
}
