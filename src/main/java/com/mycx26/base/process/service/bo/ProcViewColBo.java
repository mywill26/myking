package com.mycx26.base.process.service.bo;

import lombok.Data;

import java.io.Serializable;

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
}
