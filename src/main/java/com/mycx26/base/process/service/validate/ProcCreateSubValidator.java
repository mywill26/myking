package com.mycx26.base.process.service.validate;

import com.mycx26.base.process.service.bo.ProcParamWrapper;

/**
 * Process create sub form custom validate.
 *
 * Created by mycx26 on 2020/10/10.
 */
@FunctionalInterface
public interface ProcCreateSubValidator {

    void validateSubForm(ProcParamWrapper procParamWrapper);
}
