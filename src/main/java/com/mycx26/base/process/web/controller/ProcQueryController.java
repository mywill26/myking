package com.mycx26.base.process.web.controller;


import com.mycx26.base.process.service.ProcQueryService;
import com.mycx26.base.service.dto.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author mycx26
 * @since 2020-06-21
 */
@RestController
@RequestMapping("${proc-prefix}/query")
public class ProcQueryController {

    @Resource
    private ProcQueryService procQueryService;

    @RequestMapping("getViewKeyByProcInstIdAndNodeKey")
    public Message<String> getViewKeyByProcInstIdAndNodeKey(String procInstId, String nodeKey) {
        return Message.success(procQueryService.getViewKeyByProcInstIdAndNodeKey(procInstId, nodeKey));
    }

    @RequestMapping("getDetailViewKeyByProcInstId")
    public Message<String> getDetailViewKeyByProcInstId(String procInstId) {
        return Message.success(procQueryService.getDetailViewKeyByProcInstId(procInstId));
    }

    @RequestMapping("getDetailViewKeyByFlowNo")
    public Message<String> getDetailViewKeyByFlowNo(String flowNo) {
        return Message.success(procQueryService.getDetailViewKeyByFlowNo(flowNo));
    }
}
