package com.mycx26.base.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mycx26.base.process.entity.ProcNodeVar;

import java.util.List;

/**
 * <p>
 * 流程节点变量映射 服务类
 * </p>
 *
 * @author mycx26
 * @since 2020-06-29
 */
public interface ProcNodeVarService extends IService<ProcNodeVar> {

    List<ProcNodeVar> getByProcDefKeyAndNodeKey(String procDefKey, String nodeKey);
}
