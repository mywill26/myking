package com.mycx26.base.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mycx26.base.process.entity.ProcColumn;

import java.util.List;

/**
 * <p>
 * 流程列配置 服务类
 * </p>
 *
 * @author mycx26
 * @since 2020-06-21
 */
public interface ProcColumnService extends IService<ProcColumn> {

    List<ProcColumn> getByTblName(String tblName);

    List<ProcColumn> getAddsByTblName(String tblName);
}
