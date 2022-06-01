package com.mycx26.base.process.service;

import com.mycx26.base.process.entity.ProcNode;
import com.mycx26.base.process.service.bo.ProcTask;
import com.mycx26.base.process.service.bo.ProcessAction;
import com.mycx26.base.process.service.bo.ProcessCancel;
import com.mycx26.base.process.service.bo.ProcessLog;
import com.mycx26.base.process.service.bo.ProcessStart;
import com.mycx26.base.process.service.bo.ToDoHeader;
import com.mycx26.base.process.service.query.Prophet;
import com.mycx26.base.process.service.query.TaskQuery;
import com.mycx26.base.service.dto.PageData;

import java.util.List;

/**
 * Interaction with process engine.
 *
 * Created by mycx26 on 2020/5/26.
 */
public interface ProcEngineService {

    // syn process engine node info
    List<ProcNode> getNodes(String procDefKey);

    // start process and return <*** process instance id ***>
    String startProcess(ProcessStart processStart);

    // get next node keys
    List<String> getNextKeys(Prophet prophet);

    // get to do headers by process
    List<ToDoHeader> getToDoHeaders(String userId);

    // get to do list page
    PageData<ProcTask> pageToDoTasks(TaskQuery taskQuery);

    // get to do list
    List<ProcTask> getToDoTasks(TaskQuery taskQuery);

    /**
     * get running tasks by process instance id
     *
     * @param procInstId process instance id
     * @return running tasks
     */
    List<ProcTask> getRunningTasks(String procInstId);

    // approve
    void approve(ProcessAction processAction);

    // reject previous
    void rejectPrevious(ProcessAction processAction);

    // reject first
    void rejectFirst(ProcessAction processAction);

    // cancel process
    void cancelProcess(ProcessCancel processCancel);

    // restart process
    void restartProcess(ProcessStart processStart);

    // get process logs
    List<ProcessLog> getProcLogs(String procInstId);

    // get process logs with predict
    List<ProcessLog> getProcLogsWithPredict(String procInstId);
}
