package com.mycx26.base.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mycx26.base.enump.Yn;
import com.mycx26.base.process.entity.ProcNodeVar;
import com.mycx26.base.process.mapper.ProcNodeVarMapper;
import com.mycx26.base.process.service.ProcNodeVarService;
import com.mycx26.base.util.StringUtil;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 流程节点变量映射 服务实现类
 * </p>
 *
 * @author mycx26
 * @since 2020-06-29
 */
@Service
public class ProcNodeVarServiceImpl extends ServiceImpl<ProcNodeVarMapper, ProcNodeVar> implements ProcNodeVarService {

    // todo add cache
    @Override
    public List<ProcNodeVar> getByProcDefKeyAndNodeKey(String procDefKey, String nodeKey) {
        if (StringUtil.isAnyBlank(procDefKey, nodeKey)) {
            return Collections.emptyList();
        }

        return list(new QueryWrapper<ProcNodeVar>().select("proc_def_key, node_key, var_name")
                .eq("proc_def_key", procDefKey).eq("node_key", nodeKey).eq("yn", Yn.YES.getCode()));
    }
}
