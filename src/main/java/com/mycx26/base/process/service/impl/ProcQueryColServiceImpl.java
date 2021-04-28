package com.mycx26.base.process.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mycx26.base.constant.DateConstant;
import com.mycx26.base.constant.PageConstant;
import com.mycx26.base.constant.SqlConstant;
import com.mycx26.base.constant.Symbol;
import com.mycx26.base.context.UserContext;
import com.mycx26.base.enump.ColTypeEnum;
import com.mycx26.base.enump.QueryColType;
import com.mycx26.base.enump.QueryFormType;
import com.mycx26.base.enump.Yn;
import com.mycx26.base.excel.entity.TemplateCol;
import com.mycx26.base.excel.exp.bo.ExportParam;
import com.mycx26.base.excel.exp.thread.ExportMainThreadService;
import com.mycx26.base.excel.property.BatchProperty;
import com.mycx26.base.exception.DataException;
import com.mycx26.base.exception.ParamException;
import com.mycx26.base.process.entity.ProcQueryCol;
import com.mycx26.base.process.mapper.ProcQueryColMapper;
import com.mycx26.base.process.service.ProcQueryColService;
import com.mycx26.base.process.service.bo.QueryListColHeader;
import com.mycx26.base.process.service.bo.QueryQueryCol;
import com.mycx26.base.service.EnumValueService;
import com.mycx26.base.service.ExternalEnumService;
import com.mycx26.base.service.JdbcService;
import com.mycx26.base.service.base.impl.BaseServiceImpl;
import com.mycx26.base.service.dto.PageData;
import com.mycx26.base.util.SpringUtil;
import com.mycx26.base.util.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 流程查询列配置 服务实现类
 * </p>
 *
 * @author mycx26
 * @since 2021-01-04
 */
@Service
public class ProcQueryColServiceImpl extends BaseServiceImpl<ProcQueryColMapper, ProcQueryCol> implements ProcQueryColService {

    private static final String PROC_DEF_KEY = "procDefKey";

    private static final String EXPORT_SUFFIX = "Exp";

    private static final String TRUE = "1";

    private Map<String, ExternalEnumService> eEnumMap = new HashMap<>();

    @Resource
    private JdbcService jdbcService;

    @Resource
    private EnumValueService enumValueService;

    @Resource
    private ExportMainThreadService exportMainThreadService;

    @Resource
    private BatchProperty batchProperty;

    @Override
    public List<QueryQueryCol> getQueryCols(String procDefKey) {
        if (StringUtil.isBlank(procDefKey)) {
            return Collections.emptyList();
        }

        return list(Wrappers.<ProcQueryCol>lambdaQuery().select(ProcQueryCol::getColCode,
                ProcQueryCol::getColName,
                ProcQueryCol::getColTypeCode,
                ProcQueryCol::getEnumTypeCode,
                ProcQueryCol::getDisplay)
                .eq(ProcQueryCol::getProcDefKey, procDefKey)
                .eq(ProcQueryCol::getFormTypeCode, QueryFormType.QUERY.getCode())
                .eq(ProcQueryCol::getYn, Yn.YES.getCode())
                .orderByAsc(ProcQueryCol::getOrderNo), e -> {
            QueryQueryCol queryQueryCol = new QueryQueryCol().setColCode(e.getColCode())
                    .setColName(e.getColName())
                    .setColTypeCode(e.getColTypeCode())
                    .setDisplay(e.getDisplay());
            if (QueryColType.ENUM.getCode().equals(e.getColTypeCode())) {
                queryQueryCol.setOptions(enumValueService.getOptionsByTypeCode(e.getEnumTypeCode()));
            } else if (QueryColType.EXTERNAL_ENUM.getCode().equals(e.getColTypeCode())) {
                if (null == eEnumMap.get(e.getEnumTypeCode())) {
                    eEnumMap.put(e.getEnumTypeCode(), SpringUtil.getBean(e.getEnumTypeCode()));
                }
                ExternalEnumService externalEnumService = eEnumMap.get(e.getEnumTypeCode());
                queryQueryCol.setOptions(externalEnumService.getAllOptions());
            }

            return queryQueryCol;
        }
        );
    }

    @Override
    public List<ProcQueryCol> getByProcAndFormType(String procDefKey, String formTypeCode) {
        if (StringUtil.isAnyBlank(procDefKey, formTypeCode)) {
            return Collections.emptyList();
        }

        return list(Wrappers.<ProcQueryCol>lambdaQuery().eq(ProcQueryCol::getProcDefKey, procDefKey)
                        .eq(ProcQueryCol::getFormTypeCode, formTypeCode)
                        .eq(ProcQueryCol::getYn, Yn.YES.getCode())
                        .orderByAsc(ProcQueryCol::getOrderNo)
        );
    }

    @Override
    public List<QueryListColHeader> getListColHeaders(String procDefKey) {
        List<ProcQueryCol> listCols = getByProcAndFormType(procDefKey, QueryFormType.LIST.getCode());
        if (listCols.isEmpty()) {
            return Collections.emptyList();
        }

        return listCols.stream().map(e -> new QueryListColHeader().setPropName(e.getPropName()).setColName(e.getColName()).setDisplay(e.getDisplay())).collect(Collectors.toList());
    }

    @Override
    public PageData<Map<String, Object>> getList(Map<String, Object> params) {
        String procDefKey = (String) params.get(PROC_DEF_KEY);
        if (StringUtil.isBlank(procDefKey)) {
            throw new ParamException("Process definition key is required");
        }
        params.remove(PROC_DEF_KEY);

        List<ProcQueryCol> listCols = getByProcAndFormType(procDefKey, QueryFormType.LIST.getCode());
        if (listCols.isEmpty()) {
            throw new DataException("Process query columns config error");
        }

        String tblName = listCols.get(0).getTblName();
        List<String> cols = listCols.stream().map(ProcQueryCol::getColCode).collect(Collectors.toList());
        params.putIfAbsent(PageConstant.CURRENT, PageConstant.CURRENT_1);
        params.putIfAbsent(PageConstant.SIZE, PageConstant.SIZE_10);
        Integer current = handlePageParam(params.get(PageConstant.CURRENT));
        Integer size = handlePageParam(params.get(PageConstant.SIZE));
        params.remove(PageConstant.CURRENT);
        params.remove(PageConstant.SIZE);

        StringBuilder endClause = handleQueryParams(procDefKey, params);
        String switchTblName = handleSwitchTable(procDefKey, params, endClause);
        if (StringUtil.isNotBlank(switchTblName)) {
            tblName = switchTblName;
        }
        PageData<Map<String, Object>> pageData = jdbcService.selectList(tblName, cols, params, current, size, endClause);

        if (pageData.getTotal() > 0) {
            pageData.getRecords().forEach(e -> listCols.forEach(col -> {
                if (ColTypeEnum.ENUM.getCode().equals(col.getColTypeCode())) {
                    e.put(col.getPropName(), enumValueService.getNameByTypeAndValueCode(col.getEnumTypeCode(), (String) e.get(col.getPropName())));
                } else if (ColTypeEnum.EXTERNAL_ENUM.getCode().equals(col.getColTypeCode())) {
                    if (null == eEnumMap.get(col.getEnumTypeCode())) {
                        eEnumMap.put(col.getEnumTypeCode(), SpringUtil.getBean(col.getEnumTypeCode()));
                    }
                    ExternalEnumService externalEnumService = eEnumMap.get(col.getEnumTypeCode());
                    e.put(col.getPropName(), externalEnumService.getNameByCode((String) e.get(col.getPropName())));
                }
            })
            );
        }

        return pageData;
    }

    private Integer handlePageParam(Object object) {
        if (object instanceof String) {
            return Integer.parseInt((String) object);
        }
        if (object instanceof Integer) {
            return (Integer) object;
        }

        return null;
    }

    private StringBuilder handleQueryParams(String procDefKey, Map<String, Object> params) {
        List<ProcQueryCol> queryCols = getByProcAndFormType(procDefKey, QueryFormType.QUERY.getCode());
        StringBuilder endClause = new StringBuilder();

        queryCols.forEach(e -> {
            if (null == params.get(e.getColCode())) {
                params.remove(e.getColCode());
                return;
            }

            if (QueryColType.DATE.getCode().equals(e.getColTypeCode())) {
                params.put(String.format(SqlConstant.DATE, e.getColCode()), params.get(e.getColCode()));
                params.remove(e.getColCode());
            } else if (QueryColType.DATE_RANGE.getCode().equals(e.getColTypeCode())) {
                String[] split = ((String) params.get(e.getColCode())).split(Symbol.COMMA, 3);
                String startDate = split[0];
                String endDate = split[1];
                if (startDate.equals(endDate)) {        // more effective
                    params.put(String.format(SqlConstant.DATE, e.getColCode()), startDate);
                    params.remove(e.getColCode());
                } else {
                    endClause.append(SqlConstant.AND)
                            .append(e.getColCode()).append(Symbol.GE).append(Symbol.SINGLE_QUOTE).append(startDate).append(DateConstant.DAY_START).append(Symbol.SINGLE_QUOTE)
                            .append(SqlConstant.AND)
                            .append(e.getColCode()).append(Symbol.LE).append(Symbol.SINGLE_QUOTE).append(endDate).append(DateConstant.DAY_END).append(Symbol.SINGLE_QUOTE);
                    params.remove(e.getColCode());
                }

            } else if (QueryColType.STRING_FUZZY.getCode().equals(e.getColTypeCode())) {
                endClause.append(SqlConstant.AND)
                        .append(e.getColCode())
                        .append(SqlConstant.LIKE)
                        .append(String.format(SqlConstant.LIKE_ALL, params.get(e.getColCode())));
                params.remove(e.getColCode());
            }
        });

        return endClause;
    }

    private String handleSwitchTable(String procDefKey, Map<String, Object> params, StringBuilder endClause) {
        List<ProcQueryCol> queryCols = getByProcAndFormType(procDefKey, QueryFormType.QUERY.getCode());
        String tblName = null;
        for (ProcQueryCol queryCol : queryCols) {
            if (StringUtil.isNotBlank(queryCol.getTblName()) && params.get(queryCol.getColCode()) != null) {
                tblName = queryCol.getTblName();
                if (StringUtil.isNotBlank(queryCol.getEndClause())) {
                    endClause.append(Symbol.SPACE).append(queryCol.getEndClause());
                }
                break;
            }
        }

        return tblName;
    }

    @Override
    public void exp(Map<String, Object> params) {
        params.remove(PageConstant.CURRENT);
        params.remove(PageConstant.SIZE);
        String procDefKey = (String) params.get(PROC_DEF_KEY);
        if (StringUtil.isBlank(procDefKey)) {
            throw new ParamException("Process definition key is required");
        }
        exportMainThreadService.startExp(procDefKey + EXPORT_SUFFIX, UserContext.getUserId(), params);
    }

    public List<Map<String, Object>> expRead(int current, ExportParam exportParam) {
        Map<String, Object> params = exportParam.getParams();
        Map<String, Object> queryParams = new HashMap<>(params);
        String procDefKey = (String) params.get(PROC_DEF_KEY);
        List<TemplateCol> cols = exportParam.getTemplate().getCols();
        List<String> colCodes = cols.stream().map(TemplateCol::getColName).collect(Collectors.toList());
        StringBuilder endClause = handleQueryParams(procDefKey, queryParams);
        queryParams.remove(PROC_DEF_KEY);
        queryParams.put(TRUE, TRUE);
        PageData<Map<String, Object>> pageData = jdbcService.selectList(cols.get(0).getTableName(), colCodes,
                queryParams, current, batchProperty.getExpBatchCount(), endClause);

        return pageData.getRecords();
    }
}
