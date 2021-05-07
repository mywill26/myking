package com.mycx26.base.process.web.controller;

import com.mycx26.base.context.UserContext;
import com.mycx26.base.process.service.ProcCoreService;
import com.mycx26.base.process.service.bo.ApproveView;
import com.mycx26.base.process.service.bo.ApproveWrapper;
import com.mycx26.base.process.service.bo.ProcParamWrapper;
import com.mycx26.base.process.service.bo.ProcToDo;
import com.mycx26.base.process.service.bo.ToDoHeader;
import com.mycx26.base.process.service.bo.ToDoQueryCol;
import com.mycx26.base.process.service.query.ApproveViewQuery;
import com.mycx26.base.process.service.query.TaskQuery;
import com.mycx26.base.service.dto.Message;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by mycx26 on 2020/7/11.
 */
@RequestMapping("flow/core")
@RestController
public class ProcCoreController {

    @Resource
    private ProcCoreService procCoreService;

    @RequestMapping("getToDoHeaders")
    public Message<List<ToDoHeader>> getToDoHeaders() {
        return Message.success(procCoreService.getToDoHeaders(UserContext.getUserId()));
    }

    @RequestMapping("getToDo")
    public Message<ProcToDo> getToDo(@RequestBody TaskQuery taskQuery) {
        taskQuery.setUserId(UserContext.getUserId());
        return Message.success(procCoreService.getToDo(taskQuery));
    }

    @RequestMapping("getToDoQueryCols")
    public Message<List<ToDoQueryCol>> getToDoQueryCols(String procDefKey) {
        return Message.success(procCoreService.getToDoQueryCols(procDefKey));
    }

    @RequestMapping("getApproveView")
    public Message<ApproveView> getApproveView(ApproveViewQuery approveViewQuery) {
        return Message.success(procCoreService.getApproveView(approveViewQuery));
    }

    @RequestMapping("getDetailView")
    public Message<ApproveView> getDetailView(String procInstId) {
        return Message.success(procCoreService.getDetailView(procInstId));
    }

    @PostMapping("approve")
    public Message approve(ApproveWrapper approveWrapper) {
        approveWrapper.setUserId(UserContext.getUserId());
        procCoreService.approve(approveWrapper);

        return Message.success();
    }

    @PostMapping("rejectPrevious")
    public Message rejectPrevious(ApproveWrapper approveWrapper) {
        approveWrapper.setUserId(UserContext.getUserId());
        procCoreService.rejectPrevious(approveWrapper);

        return Message.success();
    }

    @PostMapping("rejectFirst")
    public Message rejectFirst(ApproveWrapper approveWrapper) {
        approveWrapper.setUserId(UserContext.getUserId());
        procCoreService.rejectFirst(approveWrapper);

        return Message.success();
    }

    @PostMapping("create")
    public Message create(ProcParamWrapper procParamWrapper) {
        procParamWrapper.setUserId(UserContext.getUserId());
        procCoreService.create(procParamWrapper);

        return Message.success();
    }
}
