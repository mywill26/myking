package com.mycx26.base.process.service.bo;

import com.mycx26.base.process.entity.ProcDef;
import com.mycx26.base.service.bo.ParamWrapper;

/**
 * Created by mycx26 on 2020/7/17.
 */
public class ApproveWrapper extends ParamWrapper {

    private String procInstId;

    private String taskId;

    private String userId;

    private String nodeKey;

    private String comment;

    // =======================================>

    private ProcDef procDef;

    private String flowNo;

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNodeKey() {
        return nodeKey;
    }

    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ProcDef getProcDef() {
        return procDef;
    }

    public void setProcDef(ProcDef procDef) {
        this.procDef = procDef;
    }

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    @Override
    public String toString() {
        return "ApproveWrapper{" +
                "procInstId='" + procInstId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", userId='" + userId + '\'' +
                ", nodeKey='" + nodeKey + '\'' +
                ", comment='" + comment + '\'' +
                ", procDef=" + procDef +
                ", flowNo='" + flowNo + '\'' +
                '}';
    }
}
