package com.mycx26.base.process.service.bo;

import lombok.Data;

import java.util.List;

/**
 * @author mycx26
 * @date 2022/8/9
 */
@Data
public class CombineViewBo {

    private String viewKey;

    private String viewName;

    private Integer orderNo;

    List<ProcViewColBo> viewCols;
}
