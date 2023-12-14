package com.mycx26.base.process.service.impl;

import com.mycx26.base.exception.DataException;
import com.mycx26.base.exception.ParamException;
import com.mycx26.base.process.entity.ProcDef;
import com.mycx26.base.process.entity.ProcInst;
import com.mycx26.base.process.entity.ProcLock;
import com.mycx26.base.process.entity.ProcNode;
import com.mycx26.base.process.enump.InstanceStatus;
import com.mycx26.base.process.exception.TaskNotExistException;
import com.mycx26.base.process.service.ProcBaseService;
import com.mycx26.base.process.service.ProcCoreService;
import com.mycx26.base.process.service.ProcDefService;
import com.mycx26.base.process.service.ProcEngineService;
import com.mycx26.base.process.service.ProcExtendedService;
import com.mycx26.base.process.service.ProcFlowNoService;
import com.mycx26.base.process.service.ProcFormService;
import com.mycx26.base.process.service.ProcInstService;
import com.mycx26.base.process.service.ProcLockService;
import com.mycx26.base.process.service.ProcNodeHandler;
import com.mycx26.base.process.service.ProcNodeService;
import com.mycx26.base.process.service.bo.ApproveWrapper;
import com.mycx26.base.process.service.bo.ProcParamWrapper;
import com.mycx26.base.process.service.bo.ProcTask;
import com.mycx26.base.process.service.bo.ProcessAction;
import com.mycx26.base.process.service.bo.ProcessCancel;
import com.mycx26.base.process.service.bo.ProcessStart;
import com.mycx26.base.process.service.bo.ReassignWrapper;
import com.mycx26.base.process.service.bo.TaskReassign;
import com.mycx26.base.process.service.handler.ProcCreatePreHandler;
import com.mycx26.base.service.ExternalUserService;
import com.mycx26.base.util.CollectionUtil;
import com.mycx26.base.util.ExpAssert;
import com.mycx26.base.util.SpringUtil;
import com.mycx26.base.util.StringUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by mycx26 on 2020/6/21.
 */
@Service
public class ProcCoreServiceImpl implements ProcCoreService {

    @Resource
    private ProcFormService procFormService;

    @Resource
    private ProcInstService procInstService;

    @Lazy
    @Resource
    private ProcFlowNoService procFlowNoService;

    @Resource
    private ProcEngineService procEngineService;

    @Resource
    private ProcNodeService procNodeService;

    @Lazy
    @Resource
    private ExternalUserService externalUserService;

    @Resource
    private ProcDefService procDefService;

    @Resource
    private ProcLockService procLockService;

    @Resource(name = "procBaseService")
    private ProcBaseService procBaseService;

    @Resource
    private ProcExtendedService procExtendedService;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private ProcCoreService procCoreService;

    @PostConstruct
    private void init() {
        procCoreService = SpringUtil.getBean(ProcCoreServiceImpl.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String create(ProcParamWrapper procParamWrapper) {
        createBaseValidate(procParamWrapper);

        ProcDef procDef = procDefService.getByKey(procParamWrapper.getProcDefKey());
        handleFlowNo(procParamWrapper, procDef);
        handleSubject(procParamWrapper, procDef);

        // create pre-handle
        ProcBaseService procBaseService = SpringUtil.getBean2(procParamWrapper.getProcDefKey() + ProcBaseService.SUFFIX);
        if (procBaseService instanceof ProcCreatePreHandler) {
            ((ProcCreatePreHandler)procBaseService).createPreHandle(procParamWrapper);
        }
        // add process instance
        ProcInst procInst = addProcInst(procParamWrapper, procDef);
        // handle main form
        if (StringUtil.isNotBlank(procDef.getMainForm())) {
            procFormService.addMainForm(procDef.getMainForm(), procParamWrapper);
        }
        // handle sub form, if sub form configured, suit for simple process
        if (StringUtil.isNotBlank(procDef.getSubForm())) {
            procFormService.addSubForm(procDef.getSubForm(), procParamWrapper);
            createResourceLock(procParamWrapper, procDef);
        }

        // last, interaction with pe
        return handleProcEngine(procParamWrapper, procInst);
    }

    private void createBaseValidate(ProcParamWrapper procParamWrapper) {
        StringBuilder sb = validateProcDefAndUser(procParamWrapper);
        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
            throw new ParamException(sb.toString());
        }
    }

    private void handleFlowNo(ProcParamWrapper procParamWrapper, ProcDef procDef) {
        // consider without flow number condition
        if (StringUtil.isBlank(procParamWrapper.getFlowNo())) {
            procParamWrapper.setFlowNo(procFlowNoService.getFlowNo(procDef.getFlowNoPrefix()));
        }
    }

    private void handleSubject(ProcParamWrapper procParamWrapper, ProcDef procDef) {
        if (StringUtil.isBlank(procParamWrapper.getProcInstName())) {
            String username = externalUserService.getNameByUserId(procParamWrapper.getUserId());
            procParamWrapper.setProcInstName(username + "的" + procDef.getProcDefName() + "申请");
        }
    }

    private ProcInst addProcInst(ProcParamWrapper procParamWrapper, ProcDef procDef) {
        if (StringUtil.isBlank(procParamWrapper.getProcInstStatusCode())) {
            procParamWrapper.setProcInstStatusCode(InstanceStatus.RUN.getCode());
        }

        ProcInst procInst = new ProcInst().setFlowNo(procParamWrapper.getFlowNo())
                .setProcInstName(procParamWrapper.getProcInstName())
                .setProcDefKey(procDef.getProcDefKey())
                .setStatusCode(procParamWrapper.getProcInstStatusCode())
                .setSideCode(procParamWrapper.getSideCode())
                .setCreatorId(procParamWrapper.getUserId());
        procInstService.add(procInst);

        return procInst;
    }

    private void createResourceLock(ProcParamWrapper procParamWrapper, ProcDef procDef) {
        if (procDef.getLockResource()    // resource lock if configured
                && CollectionUtil.isNotEmpty(procParamWrapper.getSubItems())) {     // sub form data exist
            procLockService.lock(procParamWrapper.getSubItems().stream()
                    .filter(e -> StringUtil.isNotBlank((String) e.get(procDef.getResourceKey()))).map(e -> new ProcLock()
                            .setResourceId((String) e.get(procDef.getResourceKey()))
                            .setFlowNo(procParamWrapper.getFlowNo()))
                    .collect(Collectors.toList())
            );
        }
    }

    private String handleProcEngine(ProcParamWrapper procParamWrapper, ProcInst procInst) {
        String procInstId = null;
        if (InstanceStatus.RUN.getCode().equals(procParamWrapper.getProcInstStatusCode())) {
            procInstId = start(procParamWrapper);
            procInst.setProcInstId(procInstId);
            procExtendedService.completeProcInstId(procInst);
        }

        return procInstId;
    }

    @Override
    public String start(ProcParamWrapper procParamWrapper) {
        startValidate(procParamWrapper);

        ProcBaseService service = SpringUtil.getBean2(procParamWrapper.getProcDefKey() + ProcBaseService.SUFFIX);
        if (null == service) {
            service = procBaseService;
        }
        Map<String, Object> vars = service.setStartVar(procParamWrapper);

        ProcDef procDef = procDefService.getByKey(procParamWrapper.getProcDefKey());
        ProcessStart start = new ProcessStart()
                .setProcDefKey(procDef.getEngineKey())
                .setProcDefId(procParamWrapper.getProcDefId())
                .setName(procParamWrapper.getProcInstName())
                .setCreatorId(procParamWrapper.getUserId())
                .setBusinessKey(procParamWrapper.getFlowNo())
                .setVars(vars);

        String procInstId = procEngineService.startProcess(start);
        ProcBaseService finalService = service;
        threadPoolTaskExecutor.execute(() -> finalService.afterCreate(procParamWrapper));
        return procInstId;
    }

    private void startValidate(ProcParamWrapper procParamWrapper) {
        StringBuilder sb = validateProcDefAndUser(procParamWrapper);
        if (StringUtil.isBlank(procParamWrapper.getProcInstName())) {
            StringUtil.append(sb, "Process instance name is required");
        }

        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
            throw new ParamException(sb.toString());
        }
    }

    private StringBuilder validateProcDefAndUser(ProcParamWrapper procParamWrapper) {
        StringBuilder sb = new StringBuilder();

        if (StringUtil.isBlank(procParamWrapper.getProcDefKey())) {
            StringUtil.append(sb, "Process definition key is required");
        } else {
            ProcDef procDef = procDefService.getByKey(procParamWrapper.getProcDefKey());
            if (null == procDef) {
                throw new ParamException("Process definition key not exist");
            }
        }
        if (StringUtil.isBlank(procParamWrapper.getUserId())) {
            StringUtil.append(sb, "User id is required");
        }

        return sb;
    }

    @Override
    public void approve(ApproveWrapper approveWrapper) {
        Map<String, Object> vars = procCoreService.doApprove(approveWrapper);

        ProcessAction processAction = new ProcessAction()
                .setProcInstId(approveWrapper.getProcInstId())
                .setUserId(approveWrapper.getUserId())
                .setTaskId(approveWrapper.getTaskId())
                .setComment(approveWrapper.getComment())
                .setVars(vars);

        procEngineService.approve(processAction);

        threadPoolTaskExecutor.execute(() -> {
            ProcNode procNode = procNodeService.getByProcDefKeyAndNodeKey(
                    approveWrapper.getProcDef().getProcDefKey(), approveWrapper.getNodeKey());
            ProcNodeHandler handler = SpringUtil.getBean2(procNode.getNodeHandler());
            if (handler != null) {
                handler.afterApprove(approveWrapper);
            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> doApprove(ApproveWrapper approveWrapper) {
        ProcNodeHandler handler = preHandle(approveWrapper);
        Map<String, Object> vars = Collections.emptyMap();
        if (handler != null) {
            handler.approveValidate(approveWrapper);

            handler.handleMainForm(approveWrapper);
            handler.handleSubForm(approveWrapper);
            handler.handleBizForm(approveWrapper);

            vars = handler.handleVars(approveWrapper);  // after handle form
        }

        return vars;
    }

    private ProcNodeHandler preHandle(ApproveWrapper approveWrapper) {
        ProcInst procInst = approveValidate(approveWrapper);

        approveWrapper.setFlowNo(procInst.getFlowNo());
        approveWrapper.setProcDef(procDefService.getByKey(procInst.getProcDefKey()));
        if (null == approveWrapper.getMainForm()) {
            approveWrapper.setMainForm(Collections.emptyMap());
        }

        ProcNode procNode = validateNode(approveWrapper);

        return SpringUtil.getBean2(procNode.getNodeHandler());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void rejectPrevious(ApproveWrapper approveWrapper) {
        rejectValidate(approveWrapper);
        ProcNodeHandler handler = preHandle(approveWrapper);

        if (handler != null) {
            handler.rejectPreviousHandle(approveWrapper);
        }

        ProcessAction processAction = new ProcessAction()
                .setProcInstId(approveWrapper.getProcInstId())
                .setUserId(approveWrapper.getUserId())
                .setTaskId(approveWrapper.getTaskId())
                .setComment(approveWrapper.getComment())
                .setVars(null);

        procEngineService.rejectPrevious(processAction);
    }

    private void rejectValidate(ApproveWrapper approveWrapper) {
        if (StringUtil.isBlank(approveWrapper.getComment())) {
            throw new ParamException("Comment is required");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void rejectFirst(ApproveWrapper approveWrapper) {
        rejectValidate(approveWrapper);
        ProcNodeHandler handler = preHandle(approveWrapper);

        if (handler != null) {
            handler.rejectFirstHandle(approveWrapper);
        }

        ProcessAction processAction = new ProcessAction()
                .setProcInstId(approveWrapper.getProcInstId())
                .setUserId(approveWrapper.getUserId())
                .setTaskId(approveWrapper.getTaskId())
                .setComment(approveWrapper.getComment())
                .setVars(null);

        procEngineService.rejectFirst(processAction);
    }

    private ProcInst approveValidate(ApproveWrapper approveWrapper) {
        ProcInst procInst = procInstService.getByProcInstId(approveWrapper.getProcInstId());     // which form
        ExpAssert.isFalse(null == procInst, "Process instance not exist");

        ExpAssert.isFalse(StringUtil.isBlank(approveWrapper.getTaskId()), "Task id is required");   // which task
        List<ProcTask> tasks = procEngineService.getRunningTasks(approveWrapper.getProcInstId());
        boolean flag = tasks.stream().anyMatch(e -> e.getCurTaskId().equals(approveWrapper.getTaskId()));
        if (!flag) {
            throw new TaskNotExistException("Task not exist");
        }

        ExpAssert.isFalse(StringUtil.isBlank(approveWrapper.getUserId()), "User id is required");   // who

        return procInst;
    }

    private ProcNode validateNode(ApproveWrapper approveWrapper) {
        ProcNode procNode = procNodeService.getByProcDefKeyAndNodeKey(
                approveWrapper.getProcDef().getProcDefKey(), approveWrapper.getNodeKey());
        if (null == procNode) {
            throw new DataException("Process node not exist");
        }

        return procNode;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancel(ApproveWrapper approveWrapper) {
        ProcInst procInst = cancelValidate(approveWrapper);
        ProcBaseService service = SpringUtil.getBean2(procInst.getProcDefKey() + ProcBaseService.SUFFIX);
        if (null == service) {
            service = this.procBaseService;
        }
        service.cancelHandle(approveWrapper.getProcInstId());

        ProcessCancel processCancel = new ProcessCancel()
                .setProcInstId(approveWrapper.getProcInstId())
                .setCreatorId(approveWrapper.getUserId())
                .setComment(approveWrapper.getComment());

        procEngineService.cancelProcess(processCancel);

        threadPoolTaskExecutor.execute(() -> procBaseService.afterCancelHandle(procInst.getProcInstId()));
    }

    private ProcInst cancelValidate(ApproveWrapper approveWrapper) {
        ProcInst procInst = procInstService.getByProcInstId(approveWrapper.getProcInstId());
        ExpAssert.isFalse(null == procInst, "Process instance not exist");
        ExpAssert.isFalse(StringUtil.isBlank(approveWrapper.getUserId()), "User id is required");
        assert procInst != null;
        ExpAssert.isTrue(approveWrapper.getUserId().equals(procInst.getCreatorId()), "Only creator can cancel");

        return procInst;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void reassign(ReassignWrapper reassignWrapper) {
        ProcNodeHandler handler = preHandle(reassignWrapper);
        if (handler != null) {
            handler.reassignHandle(reassignWrapper);
        }

        ExpAssert.isFalse(StringUtil.isBlank(reassignWrapper.getToUserId()), "To user id is required");

        TaskReassign reassign = new TaskReassign()
                .setTaskId(reassignWrapper.getTaskId())
                .setUserId(reassignWrapper.getUserId())
                .setComment(reassignWrapper.getComment())
                .setToUserId(reassignWrapper.getToUserId())
                .setEngineKey(reassignWrapper.getProcDef().getEngineKey());

        procEngineService.reassign(reassign);
    }
}
