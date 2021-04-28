package com.mycx26.base.process.service.bo;

import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by mycx26 on 2020/7/13.
 */
@Data
public class ProcToDo {

    private String procDefKey;

    private List<ToDoColBo> toDoCols = Collections.emptyList();

    private List<Map<String, Object>> toDoList = Collections.emptyList();

    private Integer total = 0;
}
