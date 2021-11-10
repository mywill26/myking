package com.mycx26.base.process.service;

import java.util.Map;

/**
 * @author mycx26
 * @date 2021/11/10
 */
public interface ProcUpdateService {

    /**
     * update process form by flow no
     *
     * @param flowNo flow no
     * @param tblName table name
     * @param updates updates
     */
    void updateByFlowNo(String flowNo, String tblName, Map<String, Object> updates);

    /**
     * update process form by id
     *
     * @param id id
     * @param tblName table name
     * @param updates updates
     */
    void updateById(Long id, String tblName, Map<String, Object> updates);
}
