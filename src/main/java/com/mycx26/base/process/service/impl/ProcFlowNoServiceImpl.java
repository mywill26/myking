package com.mycx26.base.process.service.impl;

import com.mycx26.base.enump.ExpireType;
import com.mycx26.base.process.service.ProcFlowNoService;
import com.mycx26.base.service.FlowNoGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by mycx26 on 2020/9/15.
 */
@Service
public class ProcFlowNoServiceImpl implements ProcFlowNoService {

    @Resource
    private FlowNoGenerator flowNoGenerator;

    @Override
    public String getFlowNo(String prefix) {
        return flowNoGenerator.baseDate(prefix, null, 5, ExpireType.TODAY);
    }
}
