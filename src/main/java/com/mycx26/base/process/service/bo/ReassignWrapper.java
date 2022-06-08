package com.mycx26.base.process.service.bo;

/**
 * @author mycx26
 * @date 2022/6/8
 */
public class ReassignWrapper extends ApproveWrapper {

    /**
     * comma separated if multiple
     */
    private String toUserId;

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }
}
