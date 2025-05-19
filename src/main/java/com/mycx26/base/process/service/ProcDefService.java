package com.mycx26.base.process.service;

import com.mycx26.base.process.entity.ProcDef;
import com.mycx26.base.process.service.query.DefQuery;
import com.mycx26.base.service.base.BaseService;
import com.mycx26.base.service.bo.SelectOption;
import com.mycx26.base.service.dto.PageData;

import java.util.List;

/**
 * <p>
 * 流程定义 服务类
 * </p>
 *
 * @author mycx26
 * @since 2020-05-22
 */
public interface ProcDefService extends BaseService<ProcDef> {

    PageData<ProcDef> getList(DefQuery defQuery);

    void add(ProcDef procDef);

    void modify(ProcDef procDef);

    List<SelectOption> getAll();

    ProcDef getByKey(String procDefKey);

    String getNameByKey(String procDefKey);

    ProcDef getByEngineKey(String engineKey);

    ProcDef getByEngineId(String engineId);

    /**
     * get by flow no prefix
     *
     * @param flowNoPrefix flow no prefix
     * @return process definition
     */
    ProcDef getByFlowNoPrefix(String flowNoPrefix);

    /**
     * get all process definition options where can be process queried
     *
     * @return process definition options
     */
    List<SelectOption> getAllQueries();
}
