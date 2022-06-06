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

    /**
     * get to do list header info include process name & count etc
     *
     * @param userId user id
     * @return to do headers
     */
    List<ToDoHeader> getToDoHeaders(String userId);

    /**
     * get to do list by process
     *
     * @param taskQuery task query
     * @return process to do
     */
    ProcToDo getToDo(TaskQuery taskQuery);

    /**
     * get to do list query columns info
     *
     * @param procDefKey process definition key
     * @return to do query columns
     */
    List<ToDoQueryCol> getToDoQueryCols(String procDefKey);

    /**
     * get process approve view by process instance id & node key
     *
     * @param approveViewQuery approve view query
     * @return approve view
     */
    ApproveView getApproveView(ApproveViewQuery approveViewQuery);

    /**
     * get process detail view by process instance id
     *
     * @param procInstId process instance id
     * @return detail view
     */
    ApproveView getDetailView(String procInstId);

    /**
     * get process detail view by flow no
     *
     * @param flowNo flow no
     * @return detail view
     */
    ApproveView getDetailView2(String flowNo);

    /**
     * get approve view with process view col info
     *
     * @param approveViewQuery approve view query
     * @return approve view
     */
    ApproveView getApproveViewWithViewCol(ApproveViewQuery approveViewQuery);

    /**
     * get detail view with process view col info
     *
     * @param procInstId process instance id
     * @return detail view
     */
    ApproveView getDetailViewWithViewCol(String procInstId);
}
