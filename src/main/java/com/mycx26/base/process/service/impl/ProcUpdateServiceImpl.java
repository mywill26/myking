package com.mycx26.base.process.service.impl;

import com.mycx26.base.process.constant.ProcConstant;
import com.mycx26.base.process.service.ProcUpdateService;
import com.mycx26.base.service.JdbcService;
import com.mycx26.base.util.SqlUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mycx26
 * @date 2021/11/10
 */
@Service
public class ProcUpdateServiceImpl implements ProcUpdateService {

    @Resource
    private JdbcService jdbcService;

    @Override
    public void updateByFlowNo(String flowNo, String tblName, Map<String, Object> updates) {
        Map<String, Object> clauses = new HashMap<>(1);
        clauses.put(SqlUtil.camelToUnderline(ProcConstant.FLOW_NO), flowNo);

        jdbcService.update(tblName, updates, clauses);
    }

    @Override
    public void updateById(Long id, String tblName, Map<String, Object> updates) {
        Map<String, Object> clauses = new HashMap<>(1);
        clauses.put(SqlUtil.camelToUnderline(ProcConstant.ID), id);

        jdbcService.update(tblName, updates, clauses);
    }
}
