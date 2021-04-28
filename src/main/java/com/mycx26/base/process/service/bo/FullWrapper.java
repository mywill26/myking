package com.mycx26.base.process.service.bo;

import com.mycx26.base.process.entity.ProcInst;
import com.mycx26.base.service.bo.ParamWrapper;

/**
 * Created by mycx26 on 2020/7/30.
 */
public class FullWrapper extends ParamWrapper {

    private ProcInst procInst;

    public ProcInst getProcInst() {
        return procInst;
    }

    public void setProcInst(ProcInst procInst) {
        this.procInst = procInst;
    }
}
