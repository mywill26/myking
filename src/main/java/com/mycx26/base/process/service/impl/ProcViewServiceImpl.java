package com.mycx26.base.process.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mycx26.base.constant.SqlConstant;
import com.mycx26.base.constant.Symbol;
import com.mycx26.base.enump.QueryColType;
import com.mycx26.base.enump.QueryFormType;
import com.mycx26.base.enump.Yn;
import com.mycx26.base.exception.DataException;
import com.mycx26.base.process.constant.ProcConstant;
import com.mycx26.base.process.entity.CombineView;
import com.mycx26.base.process.entity.ProcDef;
import com.mycx26.base.process.entity.ProcFormView;
import com.mycx26.base.process.entity.ProcInst;
import com.mycx26.base.process.entity.ProcNode;
import com.mycx26.base.process.entity.ProcViewCol;
import com.mycx26.base.process.entity.ToDoCol;
import com.mycx26.base.process.enump.ProcFormType;
import com.mycx26.base.process.enump.ProcViewColType;
import com.mycx26.base.process.service.CombineViewService;
import com.mycx26.base.process.service.ProcBaseService;
import com.mycx26.base.process.service.ProcDefService;
import com.mycx26.base.process.service.ProcEngineService;
import com.mycx26.base.process.service.ProcExtendedService;
import com.mycx26.base.process.service.ProcFormViewService;
import com.mycx26.base.process.service.ProcInstService;
import com.mycx26.base.process.service.ProcNodeService;
import com.mycx26.base.process.service.ProcViewColService;
import com.mycx26.base.process.service.ProcViewService;
import com.mycx26.base.process.service.ToDoColService;
import com.mycx26.base.process.service.bo.ApproveView;
import com.mycx26.base.process.service.bo.CombineViewBo;
import com.mycx26.base.process.service.bo.ProcTask;
import com.mycx26.base.process.service.bo.ProcToDo;
import com.mycx26.base.process.service.bo.ProcViewColBo;
import com.mycx26.base.process.service.bo.ProcessLog;
import com.mycx26.base.process.service.bo.ToDoColBo;
import com.mycx26.base.process.service.bo.ToDoHeader;
import com.mycx26.base.process.service.bo.ToDoQueryCol;
import com.mycx26.base.process.service.query.ApproveViewQuery;
import com.mycx26.base.process.service.query.TaskQuery;
import com.mycx26.base.process.service.resolver.CombineViewResolver;
import com.mycx26.base.process.service.resolver.LightViewResolver;
import com.mycx26.base.process.service.resolver.ProcViewMainResolver;
import com.mycx26.base.process.service.resolver.ProcViewSubResolver;
import com.mycx26.base.service.EnumValueService;
import com.mycx26.base.service.ExternalEnumService;
import com.mycx26.base.service.ExternalUserService;
import com.mycx26.base.service.JdbcService;
import com.mycx26.base.service.bo.ContextUser;
import com.mycx26.base.service.bo.ParamWrapper;
import com.mycx26.base.util.CollectionUtil;
import com.mycx26.base.util.ExpAssert;
import com.mycx26.base.util.SpringUtil;
import com.mycx26.base.util.SqlUtil;
import com.mycx26.base.util.StringUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author mycx26
 * @date 2021/5/17
 */
@Service
public class ProcViewServiceImpl implements ProcViewService {

    @Resource
    private ProcInstService procInstService;

    @Resource
    private ProcNodeService procNodeService;

    @Resource
    private ProcDefService procDefService;

    @Resource
    private ProcEngineService procEngineService;

    @Resource
    private ProcViewColService procViewColService;

    @Resource
    private EnumValueService enumValueService;

    @Resource
    private JdbcService jdbcService;

    @Resource
    private ToDoColService toDoColService;

    @Lazy
    @Resource
    private ExternalUserService externalUserService;

    @Resource
    private ProcFormViewService procFormViewService;

    @Resource
    private ProcExtendedService procExtendedService;

    @Resource
    private CombineViewService combineViewService;

    private final Map<String, ExternalEnumService> eEnumMap = new HashMap<>();

    @Override
    public List<ToDoHeader> getToDoHeaders(String userId) {
        Map<String, ToDoHeader> collect = procDefService.list(Wrappers.<ProcDef>lambdaQuery()
                .eq(ProcDef::getInternal, Yn.YES.getCode()).orderByAsc(ProcDef::getOrderNo)).stream()
                .collect(Collectors.toMap(ProcDef::getEngineKey,
                        v -> new ToDoHeader().setProcDefKey(v.getProcDefKey()).setProcDefName(v.getProcDefName()).setCount(0),
                        (e1, e2) -> e2, LinkedHashMap::new
                ));
        if (collect.isEmpty()) {
            return Collections.emptyList();
        }

        List<ToDoHeader> headers = procEngineService.getToDoHeaders(userId);
        if (headers.isEmpty()) {
            return new ArrayList<>(collect.values());
        }

        Map<String, ToDoHeader> engineCollect = headers.stream().collect(Collectors.toMap(ToDoHeader::getProcDefKey, v -> v));
        collect.forEach((k, v) -> {
            if (engineCollect.get(k) != null) {
                v.setCount(engineCollect.get(k).getCount());
            }
        });

        return new ArrayList<>(collect.values());
    }

    @Override
    public ProcToDo getToDo(TaskQuery taskQuery) {
        if (StringUtil.isAnyBlank(taskQuery.getProcDefKey(), taskQuery.getUserId())) {
            return new ProcToDo();
        }
        if (null == taskQuery.getParams()) {
            taskQuery.setParams(Collections.emptyMap());
        }

        ProcDef procDef = procDefService.getByKey(taskQuery.getProcDefKey());
        taskQuery.setProcDefKey(procDef.getEngineKey());
        List<ProcTask> tasks = procEngineService.getToDoTasks(taskQuery);
        List<ToDoCol> toDoCols = toDoColService.getByProcAndFormType(procDef.getProcDefKey(), QueryFormType.LIST.getCode());
        if (toDoCols.isEmpty()) {
            throw new DataException("To do list columns config error");
        }

        List<Map<String, Object>> toDoList = Collections.emptyList();

        if (!tasks.isEmpty()) {
            StringBuilder endClause = handleToDoQueryParams(procDef.getProcDefKey(), taskQuery.getParams());
            String tblName = toDoCols.get(0).getTblName();
            String switchTblName = handleSwitchTable(procDef.getProcDefKey(), taskQuery.getParams(), endClause);
            endClause.append(SqlConstant.ORDER_BY).append("pi.create_time");
            if (StringUtil.isNotBlank(switchTblName)) {
                tblName = switchTblName;
            }
            toDoList = jdbcService.selectListIn(tblName,
                    toDoCols.stream().map(ToDoCol::getColCode).collect(Collectors.toList()),
                    "pi.proc_inst_id",
                    tasks.stream().map(e -> (Object) e.getProcInstId()).toArray(),
                    taskQuery.getParams(),
                    endClause
            );

            Map<String, ProcTask> instIdTaskMap = tasks.stream().collect(Collectors.toMap(ProcTask::getProcInstId, v -> v, (k1, k2) -> k1));
            toDoList.forEach(item -> supplyToDo(item, instIdTaskMap, procDef));

            // translate
            Map<String, List<ToDoCol>> collect = toDoCols.stream().collect(Collectors.groupingBy(ToDoCol::getColTypeCode));
            List<ToDoCol> enumCols = collect.get(ProcViewColType.ENUM.getCode());
            List<ToDoCol> exEnumCols = collect.get(ProcViewColType.E_ENUM.getCode());
            toDoList.forEach(toDo -> {
                if (enumCols != null) {
                    enumCols.forEach(enumCol -> handleEnumCol(toDo, enumCol.getPropName(), enumCol.getEnumTypeCode()));
                }
                if (exEnumCols != null) {
                    exEnumCols.forEach(exEnumCol -> handleExEnumCol(toDo, exEnumCol.getPropName(), exEnumCol.getEnumTypeCode()));
                }
            });
        }

        List<ToDoColBo> toDoColBos = toDoCols.stream().map(e -> new ToDoColBo().setPropName(e.getPropName())
                .setColName(e.getColName())
                .setDisplay(e.getDisplay())
        ).collect(Collectors.toList());

        return new ProcToDo().setProcDefKey(taskQuery.getProcDefKey())
                .setToDoCols(toDoColBos)
                .setToDoList(toDoList)
                .setTotal(toDoList.size());
    }

    private StringBuilder handleToDoQueryParams(String procDefKey, Map<String, Object> params) {
        List<ToDoCol> queryCols = toDoColService.getByProcAndFormType(procDefKey, QueryFormType.QUERY.getCode());
        StringBuilder endClause = new StringBuilder();

        queryCols.forEach(e -> {
            if (null == params.get(e.getColCode())) {
                params.remove(e.getColCode());
                return;
            }

            if (QueryColType.STRING_FUZZY.getCode().equals(e.getColTypeCode())) {
                endClause
                        .append(SqlConstant.AND)
                        .append(e.getColCode())
                        .append(SqlConstant.LIKE)
                        .append(String.format(SqlConstant.LIKE_ALL, params.get(e.getColCode())));
                params.remove(e.getColCode());
            }
        });

        return endClause;
    }

    private String handleSwitchTable(String procDefKey, Map<String, Object> params, StringBuilder endClause) {
        List<ToDoCol> queryCols = toDoColService.getByProcAndFormType(procDefKey, QueryFormType.QUERY.getCode());
        String tblName = null;
        for (ToDoCol toDoCol : queryCols) {
            if (StringUtil.isNotBlank(toDoCol.getTblName()) && params.get(toDoCol.getColCode()) != null) {
                tblName = toDoCol.getTblName();
                if (StringUtil.isNotBlank(toDoCol.getEndClause())) {
                    endClause.append(Symbol.SPACE).append(toDoCol.getEndClause());
                }
                break;
            }
        }

        return tblName;
    }

    private void supplyToDo(Map<String, Object> item, Map<String, ProcTask> instIdTaskMap, ProcDef procDef) {
        String procInstId = (String) item.get(ProcConstant.PROC_INST_ID);
        item.put(ProcConstant.NODE_KEY, instIdTaskMap.get(procInstId).getCurTaskKey());
        item.put(ProcConstant.NODE_NAME, instIdTaskMap.get(procInstId).getCurTaskName());
        item.put(ProcConstant.TASK_ID, instIdTaskMap.get(procInstId).getCurTaskId());
        item.put(ProcConstant.VIEW_KEY, procNodeService
                .getViewKeyByProcDefKeyAndNodeKey(procDef.getProcDefKey(), instIdTaskMap.get(procInstId).getCurTaskKey()));
    }

    @Override
    public List<ToDoQueryCol> getToDoQueryCols(String procDefKey) {
        if (StringUtil.isBlank(procDefKey)) {
            return Collections.emptyList();
        }

        return toDoColService.list(Wrappers.<ToDoCol>lambdaQuery().select(ToDoCol::getColCode,
                ToDoCol::getColName,
                ToDoCol::getColTypeCode,
                ToDoCol::getEnumTypeCode,
                ToDoCol::getDisplay).eq(ToDoCol::getProcDefKey, procDefKey)
                .eq(ToDoCol::getFormTypeCode, QueryFormType.QUERY.getCode())
                .eq(ToDoCol::getYn, Yn.YES.getCode())
                .orderByAsc(ToDoCol::getOrderNo), e -> new ToDoQueryCol().setColCode(e.getColCode())
                .setColName(e.getColName())
                .setColTypeCode(e.getColTypeCode())
                .setDisplay(e.getDisplay())
        );
    }

    @Override
    public ApproveView getApproveView(ApproveViewQuery approveViewQuery) {
        if (StringUtil.isAnyBlank(approveViewQuery.getProcInstId(), approveViewQuery.getNodeKey())) {
            return null;
        }

        ProcInst procInst = procInstService.getByProcInstId(approveViewQuery.getProcInstId());
        ProcNode procNode = procNodeService
                .getByProcDefKeyAndNodeKey(procInst.getProcDefKey(), approveViewQuery.getNodeKey());

        ApproveView approveView = new ApproveView().setViewKey(procNode.getViewKey())
                .setProcInstId(approveViewQuery.getProcInstId())
                .setProcInstStatusCode(procInst.getStatusCode())
                .setProcDefKey(procInst.getProcDefKey());

        ProcDef procDef = procDefService.getByKey(procInst.getProcDefKey());
        approveView.setProcDesc(procDef.getDescription());
        approveView.setNodeTips(procNode.getTips());
        approveView.setApprove(procNode.getApprove());
        approveView.setRejectPrevious(procNode.getRejectPrevious());
        approveView.setRejectFirst(procNode.getRejectFirst());
        approveView.setReassign(procNode.getReassign());

        if (procDef.getLightView()) {
            ProcBaseService service = SpringUtil.getBean2(procDef.getProcDefKey() + ProcBaseService.SUFFIX);
            if (service instanceof LightViewResolver) {
                ((LightViewResolver)service).resolve(approveView);
            }
        } else {
            // handle process log
            approveView.setLogs(procEngineService.getProcLogs(approveViewQuery.getProcInstId()));
            handleParamWrapper(procInst, approveView);
        }

        return approveView;
    }

    private void handleParamWrapper(ProcInst procInst, ApproveView approveView) {
        ProcFormView procFormView = procFormViewService.getByViewKey(approveView.getViewKey());
        ProcDef procDef = procDefService.getByKey(procInst.getProcDefKey());
        // combine view
        if (procFormView != null && procFormView.getCombine()) {
            approveView.setViewName(procFormView.getViewName());
            handleCombineView(procInst, approveView);
        } else {
            Map<String, List<ProcViewCol>> collect = procViewColService.getByViewKey(approveView.getViewKey())
                    .stream().collect(Collectors.groupingBy(ProcViewCol::getFormTypeCode));
            ExpAssert.isFalse(collect.isEmpty(), "View config error");

            ParamWrapper paramWrapper = new ParamWrapper();
            paramWrapper.setMainForm(handleMainForm(collect.get(ProcFormType.MAIN.getCode()), procInst.getFlowNo()));
            if (StringUtil.isNotBlank(procDef.getSubForm())) {
                paramWrapper.setSubItems(handleSubForm(collect.get(ProcFormType.SUB.getCode()), procInst.getFlowNo()));
            }

            approveView.setParamWrapper(paramWrapper);
        }

        if (procFormView != null) {
            postResolve(procFormView, procDef, approveView);
        }
    }

    private void handleCombineView(ProcInst procInst, ApproveView approveView) {
        List<CombineView> views = combineViewService.getByViewKey1(approveView.getViewKey());
        ExpAssert.isFalse(CollectionUtil.isEmpty(views), "Combine view config error");
        ParamWrapper wrapper = new ParamWrapper();
        wrapper.setMainForm(new LinkedHashMap<>());
        views.forEach(e -> {
            List<ProcViewCol> cols = procViewColService.getByViewKey(e.getViewKey2());
            wrapper.getMainForm().putAll(handleMainForm(cols, procInst.getFlowNo()));
        });

        approveView.setParamWrapper(wrapper);
    }

    private void postResolve(ProcFormView procFormView, ProcDef procDef, ApproveView approveView) {
        if (StringUtil.isNotBlank(procFormView.getMainResolver())) {
            ProcViewMainResolver mainResolver = SpringUtil.getBean2(procFormView.getMainResolver());
            if (mainResolver != null) {
                mainResolver.resolve(approveView);
            }
        }
        if (StringUtil.isNoneBlank(procDef.getSubForm(), procFormView.getSubResolver())) {
            ProcViewSubResolver subResolver = SpringUtil.getBean2(procFormView.getSubResolver());
            if (subResolver != null) {
                subResolver.resolve(approveView);
            }
        }
    }

    @Override
    public ApproveView getDetailView(String procInstId) {
        ProcInst procInst = getDetailViewValidate(procInstId);
        if (null == procInst) {
            return null;
        }

        return doGetDetailView(procInst);
    }

    private ProcInst getDetailViewValidate(String procInstId) {
        ProcInst procInst = procInstService.getByProcInstId(procInstId);
        if (null == procInst) {
            throw new DataException("Process instance not exist");
        }

        return procInst;
    }

    private ApproveView doGetDetailView(ProcInst procInst) {
        assert procInst != null;
        ApproveView approveView = new ApproveView().setProcInstId(procInst.getProcInstId())
                .setProcInstStatusCode(procInst.getStatusCode()).setProcDefKey(procInst.getProcDefKey());

        ProcDef procDef = procDefService.getByKey(procInst.getProcDefKey());
        approveView.setViewKey(procDef.getDetailViewKey()).setProcDesc(procDef.getDescription());

        // set first node's tips
        ProcNode first = procNodeService.getFirst(procDef.getProcDefKey());
        approveView.setNodeTips(first.getTips());

        approveView.setCancel(procExtendedService.isCancel(procInst));

        if (procDef.getLightView()) {
            ProcBaseService service = SpringUtil.getBean2(procDef.getProcDefKey() + ProcBaseService.SUFFIX);
            if (service instanceof LightViewResolver) {
                ((LightViewResolver)service).resolve(approveView);
            }
        } else {
            approveView.setLogs(procEngineService.getProcLogs(procInst.getProcInstId()));
            handleParamWrapper(procInst, approveView);
        }

        return approveView;
    }

    private Map<String, Object> handleMainForm(List<ProcViewCol> mainCols, String flowNo) {
        // consider general view not config table column info
        if (StringUtil.isBlank(mainCols.get(0).getTblName())) {
            return Collections.emptyMap();
        }

        Map<String, Object> clauses = Maps.newHashMapWithExpectedSize(1);
        clauses.put("pi.flow_no", flowNo);
        Map<String, Object> mainForm = jdbcService.selectMap2(mainCols.get(0).getTblName(),
                mainCols.stream().map(ProcViewCol::getColCode).collect(Collectors.toList()),
                clauses
        );

        Map<String, List<ProcViewCol>> collect = mainCols.stream().collect(Collectors.groupingBy(ProcViewCol::getColTypeCode));
        List<ProcViewCol> enumCols = collect.get(ProcViewColType.ENUM.getCode());
        if (enumCols != null) {
            enumCols.forEach(enumCol -> {
                if (enumCol.getEditable() != null && !enumCol.getEditable()) {
                    handleEnumCol(mainForm, enumCol.getPropName(), enumCol.getEnumTypeCode());
                }
            });
        }
        List<ProcViewCol> eEnumCols = collect.get(ProcViewColType.E_ENUM.getCode());
        if (eEnumCols != null) {
            eEnumCols.forEach(eEnumCol -> {
                if (eEnumCol.getEditable() != null && !eEnumCol.getEditable()) {
                    handleExEnumCol(mainForm, eEnumCol.getPropName(), eEnumCol.getEnumTypeCode());
                }
            });
        }

        return mainForm;
    }

    private void handleEnumCol(Map<String, Object> item, String procName, String enumTypeCode) {
        if (item.get(procName) != null) {
            item.put(procName, enumValueService.getNameByTypeAndValueCode(enumTypeCode, (String) item.get(procName)));
        }
    }

    private void handleExEnumCol(Map<String, Object> item, String procName, String enumTypeCode) {
        if (item.get(procName) != null) {
            if (null == eEnumMap.get(enumTypeCode)) {
                eEnumMap.put(enumTypeCode, SpringUtil.getBean(enumTypeCode));
            }
            ExternalEnumService externalEnumService = eEnumMap.get(enumTypeCode);
            item.put(procName, externalEnumService.getNameByCode((String) item.get(procName)));
        }
    }

    private List<Map<String, Object>> handleSubForm(List<ProcViewCol> subCols, String flowNo) {
        Map<String, Object> clauses = Maps.newHashMapWithExpectedSize(1);
        clauses.put(SqlUtil.camelToUnderline(ProcConstant.FLOW_NO), flowNo);
        List<Map<String, Object>> subItems = jdbcService.selectList2(subCols.get(0).getTblName(),
                subCols.stream().map(ProcViewCol::getColCode).collect(Collectors.toList()),
                clauses
        );

        Map<String, List<ProcViewCol>> collect = subCols.stream().collect(Collectors.groupingBy(ProcViewCol::getColTypeCode));

        List<ProcViewCol> enumCols = collect.get(ProcViewColType.ENUM.getCode());
        if (enumCols != null) {
            subItems.forEach(item -> enumCols.forEach(enumCol -> handleEnumCol(item, enumCol.getPropName(), enumCol.getEnumTypeCode())));
        }
        List<ProcViewCol> eEnumCols = collect.get(ProcViewColType.E_ENUM.getCode());
        if (eEnumCols != null) {
            subItems.forEach(item -> eEnumCols.forEach(eEnumCol -> handleExEnumCol(item, eEnumCol.getPropName(), eEnumCol.getEnumTypeCode())));
        }

        return subItems;
    }

    @Override
    public ApproveView getDetailView2(String flowNo) {
        ProcInst procInst = getDetailView2Validate(flowNo);
        if (null == procInst) {
            return null;
        }

        return doGetDetailView(procInst);
    }

    private ProcInst getDetailView2Validate(String flowNo) {
        if (StringUtil.isBlank(flowNo)) {
            return null;
        }
        ProcInst procInst = procInstService.getByFlowNo(flowNo);
        if (null == procInst) {
            throw new DataException("Process instance not exist");
        }

        return procInst;
    }

    @Override
    public ApproveView getApproveViewWithViewCol(ApproveViewQuery approveViewQuery) {
        ApproveView approveView = getApproveView(approveViewQuery);     // separate
        if (null == approveView) {
            return null;
        }

        handleProcLog(approveView);
        return addViewCol(approveView);
    }

    private void handleProcLog(ApproveView approveView) {
        if (CollectionUtil.isEmpty(approveView.getLogs())) {
            return;
        }

        approveView.getLogs().forEach(e -> {
            ContextUser user = externalUserService.getByUserId(e.getUserId());
            e.setAvatarUrl(user != null ? user.getAvatarUrl() : null);
        });

        List<ProcessLog> predictLogs = procEngineService.getProcLogsWithPredict(approveView.getProcInstId());
        if (!predictLogs.isEmpty()) {
            approveView.getLogs().addAll(predictLogs);
        }
    }

    private ApproveView addViewCol(ApproveView approveView) {
        ProcFormView procFormView = procFormViewService.getByViewKey(approveView.getViewKey());
        // combine view
        if (procFormView != null && procFormView.getCombine()) {
            handleCombineViewCol(approveView);
        } else {
            Map<String, List<ProcViewColBo>> collect = procViewColService.list(Wrappers.<ProcViewCol>lambdaQuery()
                            .select(ProcViewCol::getFormTypeCode,
                                    ProcViewCol::getPropName,
                                    ProcViewCol::getColName,
                                    ProcViewCol::getColTypeCode,
                                    ProcViewCol::getEnumTypeCode,
                                    ProcViewCol::getDisplay,
                                    ProcViewCol::getMobileDisplay,
                                    ProcViewCol::getOrderNo,
                                    ProcViewCol::getEditable)
                            .eq(ProcViewCol::getViewKey, approveView.getViewKey())
                            .eq(ProcViewCol::getYn, Yn.YES.getCode())
                            .orderByAsc(ProcViewCol::getOrderNo),
                    this::toProcViewColBo)
                    .stream().collect(Collectors.groupingBy(ProcViewColBo::getFormTypeCode));
            if (collect.get(ProcFormType.MAIN.getCode()) != null) {
                approveView.setMainViewCols(collect.get(ProcFormType.MAIN.getCode()));
            }
            if (collect.get(ProcFormType.SUB.getCode()) != null) {
                approveView.setSubViewCols(collect.get(ProcFormType.SUB.getCode()));
            }
        }

        return approveView;
    }

    private void handleCombineViewCol(ApproveView approveView) {
        List<CombineView> views = combineViewService.getByViewKey1(approveView.getViewKey());
        ExpAssert.isFalse(CollectionUtil.isEmpty(views), "Combine view config error");

        List<CombineViewBo> viewBos = Lists.newArrayListWithCapacity(views.size());
        views.forEach(e -> {
            CombineViewBo viewBo = new CombineViewBo()
                    .setViewKey(e.getViewKey2())
                    .setViewName(procFormViewService.getViewNameByViewKey(e.getViewKey2()))
                    .setOrderNo(e.getOrderNo());
            List<ProcViewColBo> colBos = procViewColService.getByViewKey(e.getViewKey2())
                    .stream().map(this::toProcViewColBo).collect(Collectors.toList());
            colBos.forEach(colBo -> {
                boolean flag = colBo.getEditable()
                        && (ProcViewColType.ENUM.getCode().equals(colBo.getColTypeCode())
                        || ProcViewColType.E_ENUM.getCode().equals(colBo.getColTypeCode()));
                if (flag) {
                    handleEnumOptions(colBo);
                }
            });
            viewBo.setViewCols(colBos);

            viewBos.add(viewBo);
        });

        approveView.setCombineViews(viewBos);

        // resolve combine
        ProcFormView formView = procFormViewService.getByViewKey(approveView.getViewKey());
        if (StringUtil.isNotBlank(formView.getCombineResolver())) {
            CombineViewResolver resolver = SpringUtil.getBean2(formView.getCombineResolver());
            if (resolver != null) {
                resolver.resolve(approveView);
            }
        }
    }

    private ProcViewColBo toProcViewColBo(ProcViewCol e) {
        return new ProcViewColBo()
                .setFormTypeCode(e.getFormTypeCode())
                .setPropName(e.getPropName())
                .setColName(e.getColName())
                .setColTypeCode(e.getColTypeCode())
                .setEnumTypeCode(e.getEnumTypeCode())
                .setDisplay(e.getDisplay())
                .setMobileDisplay(e.getMobileDisplay())
                .setOrderNo(e.getOrderNo())
                .setEditable(e.getEditable());
    }

    private void handleEnumOptions(ProcViewColBo colBo) {
        if (ProcViewColType.ENUM.getCode().equals(colBo.getColTypeCode())) {
            colBo.setOptions(enumValueService.getOptionsByTypeCode(colBo.getEnumTypeCode()));
        } else if (QueryColType.EXTERNAL_ENUM.getCode().equals(colBo.getColTypeCode())) {
            if (null == eEnumMap.get(colBo.getEnumTypeCode())) {
                eEnumMap.put(colBo.getEnumTypeCode(), SpringUtil.getBean(colBo.getEnumTypeCode()));
            }
            ExternalEnumService externalEnumService = eEnumMap.get(colBo.getEnumTypeCode());
            colBo.setOptions(externalEnumService.getAllOptions());
        }
    }

    @Override
    public ApproveView getDetailViewWithViewCol(String procInstId) {
        ApproveView approveView = getDetailView(procInstId);
        if (null == approveView) {
            return null;
        }

        handleProcLog(approveView);
        return addViewCol(approveView);
    }
}
