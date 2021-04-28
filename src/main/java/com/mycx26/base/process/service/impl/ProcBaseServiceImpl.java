package com.mycx26.base.process.service.impl;

import com.mycx26.base.process.entity.ProcDef;
import com.mycx26.base.process.entity.ProcInst;
import com.mycx26.base.process.service.ProcBaseService;
import com.mycx26.base.process.service.ProcLockService;
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
    protected ProcLockService procLockService;

    @Override
    public Map<String, Object> setStartVar(ProcParamWrapper procParamWrapper) {
        return Collections.emptyMap();
    }

    @Override
    public void endPostHandle(String procInstId) {
        ProcInst procInst = procInstService.end(procInstId);
        ProcDef procDef = procDefService.getByKey(procInst.getProcDefKey());

        if (procDef.getLockResource()) {
            List<Map<String, Object>> subItems = getSubItemsByProcInstId(procInstId, null);
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
