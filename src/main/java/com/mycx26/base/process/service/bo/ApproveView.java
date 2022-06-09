package com.mycx26.base.process.service.bo;

import com.mycx26.base.service.bo.ParamWrapper;
import lombok.Data;

import java.util.List;

/**
 * Created by mycx26 on 2020/2/29.
 */
@Data
public class ApproveView {

    private String viewKey;

    private String procInstId;

    private String procInstStatusCode;

    private String procDesc;

    private String nodeTips;

    private Boolean approve;

    private Boolean rejectPrevious;

    private Boolean rejectFirst;

    private Boolean reassign;

    private Boolean cancel;

    private ParamWrapper paramWrapper;

    private List<ProcessLog> logs;

    private List<ProcViewColBo> mainViewCols;

    private List<ProcViewColBo> subViewCols;

    private long subTotal;

    private Boolean mobile;

    private String procDefKey;
}
