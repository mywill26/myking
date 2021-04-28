package com.mycx26.base.process.service;

import com.mycx26.base.process.entity.ProcQueryCol;
import com.mycx26.base.process.service.bo.QueryListColHeader;
import com.mycx26.base.process.service.bo.QueryQueryCol;
import com.mycx26.base.service.base.BaseService;
import com.mycx26.base.service.dto.PageData;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 流程查询列配置 服务类
 * </p>
 *
 * @author mycx26
 * @since 2021-01-04
 */
public interface ProcQueryColService extends BaseService<ProcQueryCol> {

    List<QueryQueryCol> getQueryCols(String procDefKey);

    List<ProcQueryCol> getByProcAndFormType(String procDefKey, String formTypeCode);

    List<QueryListColHeader> getListColHeaders(String procDefKey);

    // common process list
    PageData<Map<String, Object>> getList(Map<String, Object> params);

    // common process data export
    void exp(Map<String, Object> params);
}
