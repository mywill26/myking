package com.mycx26.base.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mycx26.base.process.entity.ProcInst;
import com.mycx26.base.process.service.query.InstQuery;
import com.mycx26.base.service.dto.PageData;

import java.util.List;

/**
 * <p>
 * 流程实例 服务类
 * </p>
 *
 * @author mycx26
 * @since 2020-05-22
 */
public interface ProcInstService extends IService<ProcInst> {

    PageData<ProcInst> getList(InstQuery instQuery);

    void add(ProcInst procInst);

    ProcInst getByProcInstId(String procInstId);

    List<ProcInst> getByProcInstIds(List<String> procInstIds);

    ProcInst getByFlowNo(String flowNo);

    List<ProcInst> getByFlowNos(List<String> flowNos);

    void modifyInstByFlowNo(String procInstId, String flowNo);

    ProcInst end(String procInstId);
}
