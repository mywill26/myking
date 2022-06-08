package com.mycx26.base.process.service;

import com.mycx26.base.process.entity.ProcNode;
import com.mycx26.base.process.service.bo.ProcTask;
import com.mycx26.base.process.service.bo.ProcessAction;
import com.mycx26.base.process.service.bo.ProcessCancel;
import com.mycx26.base.process.service.bo.ProcessLog;
import com.mycx26.base.process.service.bo.TaskReassign;
import com.mycx26.base.process.service.bo.ProcessStart;
import com.mycx26.base.process.service.bo.ToDoHeader;
import com.mycx26.base.process.service.query.Prophet;
import com.mycx26.base.process.service.query.TaskQuery;
import com.mycx26.base.service.dto.PageData;

import java.util.List;

/**
 * service interaction with process engine
 *
 * Created by mycx26 on 2020/5/26.
 */
public interface ProcEngineService {

    /**
     * syn process engine node info
     *
     * @param procDefKey process definition key
     * @return process nodes
     */
    List<ProcNode> getNodes(String procDefKey);

    /**
     * start process and return <*** process instance id ***>
     *
     * @param processStart process start
     * @return process instance id
     */
    String startProcess(ProcessStart processStart);

    /**
     * get next node keys
     *
     * @param prophet prophet
     * @return node keys
     */
    List<String> getNextKeys(Prophet prophet);

    /**
     * get to do headers by user id
     *
     * @param userId user id
     * @return to do headers
     */
    List<ToDoHeader> getToDoHeaders(String userId);

    /**
     * get to do list page
     *
     * @param taskQuery task query
     * @return to do list
     */
    PageData<ProcTask> pageToDoTasks(TaskQuery taskQuery);

    /**
     * get to do list
     *
     * @param taskQuery task query
     * @return to do list
     */
    List<ProcTask> getToDoTasks(TaskQuery taskQuery);

    /**
     * get running tasks by process instance id
     *
     * @param procInstId process instance id
     * @return running tasks
     */
    List<ProcTask> getRunningTasks(String procInstId);

    /**
     * approve
     *
     * @param processAction process action
     */
    void approve(ProcessAction processAction);

    /**
     * reject previous
     *
     * @param processAction process action
     */
    void rejectPrevious(ProcessAction processAction);

    /**
     * reject first
     *
     * @param processAction process action
     */
    void rejectFirst(ProcessAction processAction);

    /**
     * cancel process
     *
     * @param processCancel process cancel
     */
    void cancelProcess(ProcessCancel processCancel);

    /**
     * restart process
     *
     * @param processStart process start
     */
    void restartProcess(ProcessStart processStart);

    /**
     * get process logs
     *
     * @param procInstId process instance id
     * @return process logs
     */
    List<ProcessLog> getProcLogs(String procInstId);

    /**
     * get process logs with predict
     *
     * @param procInstId process instance id
     * @return process logs
     */
    List<ProcessLog> getProcLogsWithPredict(String procInstId);

    /**
     *  reassign
     *
     * @param taskReassign task reassign
     */
    void reassign(TaskReassign taskReassign);
}
