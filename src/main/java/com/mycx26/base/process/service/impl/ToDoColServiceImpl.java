package com.mycx26.base.process.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mycx26.base.enump.Yn;
import com.mycx26.base.process.entity.ToDoCol;
import com.mycx26.base.process.mapper.ToDoColMapper;
import com.mycx26.base.process.service.ToDoColService;
import com.mycx26.base.service.base.impl.BaseServiceImpl;
import com.mycx26.base.util.StringUtil;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 待办列配置 服务实现类
 * </p>
 *
 * @author mycx26
 * @since 2020-07-13
 */
@Service
public class ToDoColServiceImpl extends BaseServiceImpl<ToDoColMapper, ToDoCol> implements ToDoColService {

    @Override
    public List<ToDoCol> getByProcAndFormType(String procDefKey, String formTypeCode) {
        if (StringUtil.isAnyBlank(procDefKey, formTypeCode)) {
            return Collections.emptyList();
        }

        return list(Wrappers.<ToDoCol>lambdaQuery().eq(ToDoCol::getProcDefKey, procDefKey)
                .eq(ToDoCol::getFormTypeCode, formTypeCode)
                .eq(ToDoCol::getYn, Yn.YES.getCode())
                .orderByAsc(ToDoCol::getOrderNo)
        );
    }
}
