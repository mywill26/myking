package com.mycx26.base.process.service;

/**
 * service accept notice from process engine
 *
 * @author mycx26
 * @date 2022/7/21
 */
public interface EngineNoticeService {

    /**
     * Accept process end notice.
     *
     * @param procInstId process instance id
     */
    void endNotice(String procInstId);

    /**
     * Accept process is rejected to first node notice.
     *
     * @param procInstId process instance id
     */
    void rejectFirstNotice(String procInstId);

    /**
     * Accept process cancel notice.
     *
     * @param procInstId process instance id
     */
    void cancelNotice(String procInstId);

    /**
     * Accept arrive in node notice
     *
     * @param procInstId process instance id
     * @param nodeKey node key
     */
    void arriveNodeNotice(String procInstId, String nodeKey);
}
