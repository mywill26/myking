package com.mycx26.base.process.service.bo;

import com.mycx26.base.service.bo.SelectOption;
import lombok.Data;

import java.util.List;

/**
 * Process query query col info.
 *
 * Created by mycx26 on 2020/7/13.
 */
@Data
public class ToDoQueryCol {

    private String colCode;

    private String colName;     // label

    private String colTypeCode;

    private List<SelectOption> options;

    private Boolean display;
}
