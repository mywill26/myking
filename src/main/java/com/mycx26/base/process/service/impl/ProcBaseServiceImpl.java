package com.mycx26.base.process.service.impl;

import com.mycx26.base.process.entity.ProcDef;
import com.mycx26.base.process.entity.ProcInst;
import com.mycx26.base.process.service.ProcBaseService;
import com.mycx26.base.process.service.ProcDefService;
import com.mycx26.base.process.service.ProcFormService;
import com.mycx26.base.process.service.ProcInstService;
import com.mycx26.base.process.service.ProcLockService;
import com.mycx26.base.process.service.ProcQueryService;
import com.mycx26.base.process.service.bo.ProcParamWrapper;
import com.mycx26.base.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Default implementation of process base service.
 * <p>
 * Created by mycx26 on 2020/9/18.
 */
@Slf4j
@Service("procBaseService")
public class ProcBaseServiceImpl extends ProcBaseService {

    @Resource
    protected ProcInstService procInstService;

    @Resource
    protected ProcDefService procDefService;

    @Resource
    protected ProcFormService procFormService;

    @Resource
    protected ProcQueryService procQueryService;

    @Resource
    protected ProcLockService procLockService;

    @Override
    public Map<String, Object> setStartVar(ProcParamWrapper procParamWrapper) {
        return Collections.emptyMap();
    }

    @Override
    public void afterCreate(ProcParamWrapper procParamWrapper) {
    }

    @Override
    public void endPostHandle(String procInstId) {
        ProcInst procInst = procInstService.end(procInstId);
        ProcDef procDef = procDefService.getByKey(procInst.getProcDefKey());

        if (procDef.getLockResource()) {
            List<Map<String, Object>> subItems = procQueryService.getSubItemsByProcInstId(procInst.getProcDefKey(), procInstId, null);
            if (subItems.isEmpty()) {
                return;
            }

            String resourceKey = procDef.getResourceKey();
            if (StringUtil.isNotBlank(resourceKey)) {
                List<String> resourceIds = subItems.stream().map(e -> (String) e.get(resourceKey)).collect(Collectors.toList());
                procLockService.unlock(resourceIds);
            }
        }
    }

    @Override
    public void rejectFirstHandle(String procInstId) {
    }
}
