package com.mycx26.base.process.service.bo;

import com.mycx26.base.service.bo.SelectOption;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProcViewColBo implements Serializable {

    private String formTypeCode;

    private String propName;

    private String colName;

    private String colTypeCode;

    private String enumTypeCode;

    private Boolean display;

    private Boolean mobileDisplay;

    private Integer orderNo;

    private Boolean editable;

    private List<SelectOption> options;
}
