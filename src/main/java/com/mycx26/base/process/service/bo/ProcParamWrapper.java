package com.mycx26.base.process.service.bo;

import com.mycx26.base.service.bo.ParamWrapper;

import javax.validation.constraints.NotBlank;

/**
 * Process **create** param wrapper.
 *
 * Created by mycx26 on 2020/6/21.
 */
public class ProcParamWrapper extends ParamWrapper {

    @NotBlank
    private String procDefKey;

    private String procDefId;

    private String flowNo;      // only draft required

    @NotBlank
    private String userId;

    private String procInstName;    // not required

    private String procInstStatusCode;

    private String sideCode;

    public String getProcDefKey() {
        return procDefKey;
    }

    public void setProcDefKey(String procDefKey) {
        this.procDefKey = procDefKey;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProcInstName() {
        return procInstName;
    }

    public void setProcInstName(String procInstName) {
        this.procInstName = procInstName;
    }

    public String getProcInstStatusCode() {
        return procInstStatusCode;
    }

    public void setProcInstStatusCode(String procInstStatusCode) {
        this.procInstStatusCode = procInstStatusCode;
    }

    public String getSideCode() {
        return sideCode;
    }

    public void setSideCode(String sideCode) {
        this.sideCode = sideCode;
    }
}
