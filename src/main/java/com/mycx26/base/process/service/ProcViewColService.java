package com.mycx26.base.process.service;

import com.mycx26.base.process.entity.ProcViewCol;
import com.mycx26.base.service.base.BaseService;

import java.util.List;

/**
 * <p>
 * 流程视图列配置 服务类
 * </p>
 *
 * @author mycx26
 * @since 2020-07-15
 */
public interface ProcViewColService extends BaseService<ProcViewCol> {

    List<ProcViewCol> getByViewKey(String viewKey);
}
