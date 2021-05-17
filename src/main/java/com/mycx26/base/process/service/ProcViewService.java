package com.mycx26.base.process.service;

import com.mycx26.base.process.service.bo.ApproveView;
import com.mycx26.base.process.service.bo.ProcToDo;
import com.mycx26.base.process.service.bo.ToDoHeader;
import com.mycx26.base.process.service.bo.ToDoQueryCol;
import com.mycx26.base.process.service.query.ApproveViewQuery;
import com.mycx26.base.process.service.query.TaskQuery;

import java.util.List;

/**
 * Process core view service to join & dispatch.
 *
 * @author mycx26
 * @date 2021/5/17
 */
public interface ProcViewService {

    // get to do list header info include process name & count etc
    List<ToDoHeader> getToDoHeaders(String userId);

    // get to do list by process
    ProcToDo getToDo(TaskQuery taskQuery);

    // get to do list query columns info
    List<ToDoQueryCol> getToDoQueryCols(String procDefKey);

    ApproveView getApproveView(ApproveViewQuery approveViewQuery);

    ApproveView getDetailView(String procInstId);

    // get approve view with process view col info
    ApproveView getApproveViewWithViewCol(ApproveViewQuery approveViewQuery);

    // get detail view with process view col info
    ApproveView getDetailViewWithViewCol(String procInstId);
}
