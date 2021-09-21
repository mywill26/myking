package com.mycx26.base.process.service.impl;

import com.mycx26.base.exception.DataException;
import com.mycx26.base.exception.ParamException;
import com.mycx26.base.process.entity.ProcDef;
import com.mycx26.base.process.entity.ProcInst;
import com.mycx26.base.process.entity.ProcLock;
import com.mycx26.base.process.entity.ProcNode;
import com.mycx26.base.process.enump.InstanceStatus;
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
import com.mycx26.base.process.service.bo.ProcessAction;
import com.mycx26.base.process.service.bo.ProcessStart;
import com.mycx26.base.process.service.handler.ProcCreatePreHandler;
import com.mycx26.base.service.ExternalUserService;
import com.mycx26.base.util.CollectionUtil;
import com.mycx26.base.util.SpringUtil;
import com.mycx26.base.util.StringUtil;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;
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

    @Resource
    private ProcFlowNoService procFlowNoService;

    @Resource
    private ProcEngineService procEngineService;

    @Resource
    private ProcNodeService procNodeService;

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

        String flowNo = procParamWrapper.getFlowNo();
        // consider without flow number condition
        if (StringUtil.isBlank(flowNo)) {
            flowNo = procFlowNoService.getFlowNo(procDef.getFlowNoPrefix());
            procParamWrapper.setFlowNo(flowNo);
        }
        if (StringUtil.isBlank(procParamWrapper.getProcInstName())) {
            String username = externalUserService.getNameByUserId(procParamWrapper.getUserId());
            procParamWrapper.setProcInstName(username + "的" + procDef.getProcDefName() + "申请");
        }

        // create pre-handle
        ProcBaseService procBaseService = SpringUtil.getBean2(procParamWrapper.getProcDefKey() + ProcBaseService.SUFFIX);
        if (procBaseService instanceof ProcCreatePreHandler) {
            ((ProcCreatePreHandler)procBaseService).createPreHandle(procParamWrapper);
        }
        // add process instance
        ProcInst procInst = new ProcInst().setFlowNo(flowNo)
                .setProcInstName(procParamWrapper.getProcInstName())
                .setProcDefKey(procDef.getProcDefKey())
                .setStatusCode(procParamWrapper.getProcInstStatusCode())
                .setCreatorId(procParamWrapper.getUserId());
        procInstService.add(procInst);
        // handle main form
        procFormService.addMainForm(procDef.getMainForm(), procParamWrapper);
        // handle sub form, if sub form configured, suit for simple process
        if (StringUtil.isNotBlank(procDef.getSubForm())) {
            procFormService.addSubForm(procDef.getSubForm(), procParamWrapper);
            createResourceLock(procParamWrapper, procDef);
        }

        // last, interaction with pe
        if (InstanceStatus.RUN.getCode().equals(procParamWrapper.getProcInstStatusCode())) {
            String procInstId = start(procParamWrapper);
            procInst.setProcInstId(procInstId);
            procExtendedService.completeProcInstId(procInst);
        }

        return flowNo;
    }

    private void createBaseValidate(ProcParamWrapper procParamWrapper) {
        StringBuilder sb = validateProcDefAndUser(procParamWrapper);
        if (StringUtil.isBlank(procParamWrapper.getProcInstStatusCode())) {
            StringUtil.append(sb, "Process instance status code is required");
        }

        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
            throw new ParamException(sb.toString());
        }
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
                .setName(procParamWrapper.getProcInstName())
                .setCreatorId(procParamWrapper.getUserId())
                .setVars(vars);

        String procInstId = procEngineService.startProcess(start);
        if (!(service instanceof ProcBaseServiceImpl)) {
            ProcBaseService finalService = service;
            threadPoolTaskExecutor.submit(() -> finalService.afterCreate(procParamWrapper));
        }
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

        approveWrapper.setFlowNo(procInst.getFlowNo());     // set flow no
        approveWrapper.setProcDef(procDefService.getByKey(procInst.getProcDefKey()));   // set process definition
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
        StringBuilder sb = new StringBuilder();
        ProcInst procInst = procInstService.getByProcInstId(approveWrapper.getProcInstId());     // which form
        if (null == procInst) {
            throw new DataException("Process instance not exist");
        }
        if (StringUtil.isBlank(approveWrapper.getTaskId())) {   // which task
            StringUtil.append(sb, "Task id is required");
        }
        if (StringUtil.isBlank(approveWrapper.getUserId())) {   // who
            StringUtil.append(sb, "User id is required");
        }

        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
            throw new ParamException(sb.toString());
        }

        return procInst;
    }

    private ProcNode validateNode(ApproveWrapper approveWrapper) {
        ProcNode procNode = procNodeService
                .getByProcDefKeyAndNodeKey(approveWrapper.getProcDef().getProcDefKey(), approveWrapper.getNodeKey());
        if (null == procNode) {
            throw new DataException("Process node not exist");
        }

        return procNode;
    }
}
