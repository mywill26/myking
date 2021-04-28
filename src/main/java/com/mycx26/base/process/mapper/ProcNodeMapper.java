package com.mycx26.base.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mycx26.base.process.entity.ProcNode;

import java.util.List;

/**
 * <p>
 * 流程节点 Mapper 接口
 * </p>
 *
 * @author mycx26
 * @since 2020-05-22
 */
public interface ProcNodeMapper extends BaseMapper<ProcNode> {

    void init(List<ProcNode> nodes);

    List<ProcNode> getByCategoryCode(String categoryCode);
}
