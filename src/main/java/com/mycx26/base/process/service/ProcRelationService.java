package com.mycx26.base.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mycx26.base.process.entity.ProcRelation;

import java.util.List;

/**
 * <p>
 * 流程关联关系 服务类
 * </p>
 *
 * @author mycx26
 * @since 2020-06-19
 */
public interface ProcRelationService extends IService<ProcRelation> {

    List<ProcRelation> getByProcInstId1(String procInstId1);
}
