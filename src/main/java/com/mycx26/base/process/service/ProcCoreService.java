package com.mycx26.base.process.service;

import com.mycx26.base.process.service.bo.ApproveView;
import com.mycx26.base.process.service.bo.ApproveWrapper;
import com.mycx26.base.process.service.bo.ProcParamWrapper;
import com.mycx26.base.process.service.bo.ProcToDo;
import com.mycx26.base.process.service.bo.ToDoHeader;
import com.mycx26.base.process.service.bo.ToDoQueryCol;
import com.mycx26.base.process.service.query.ApproveViewQuery;
import com.mycx26.base.process.service.query.TaskQuery;

import java.util.List;
import java.util.Map;

/**
 * Process flow core service to join & dispatch.
 *
 * Created by mycx26 on 2020/6/21.
 */
public interface ProcCoreService {

    // Create process general processing and return flow no.
    String create(ProcParamWrapper procParamWrapper);

    // Start process by process engine and return process instance id.
    String start(ProcParamWrapper procParamWrapper);

    // get to do list header info include process name & count etc
    List<ToDoHeader> getToDoHeaders(String userId);

    // get to do list by process
    ProcToDo getToDo(TaskQuery taskQuery);

    // get to do list query columns info
    List<ToDoQueryCol> getToDoQueryCols(String procDefKey);

    ApproveView getApproveView(ApproveViewQuery approveViewQuery);

    ApproveView getDetailView(String procInstId);

    void approve(ApproveWrapper approveWrapper);

    Map<String, Object> approvePreHandle(ApproveWrapper approveWrapper);

    void rejectPrevious(ApproveWrapper approveWrapper);

    void rejectFirst(ApproveWrapper approveWrapper);

    // get approve view with process view col info
    ApproveView getApproveViewWithViewCol(ApproveViewQuery approveViewQuery);

    // get detail view with process view col info
    ApproveView getDetailViewWithViewCol(String procInstId);
}
