package com.acuo.persist.services;

import com.acuo.persist.entity.Agreement;
import com.acuo.persist.entity.LegalEntity;

public interface LegalEntityService  extends Service<LegalEntity> {

    LegalEntity getLegalEntity(Agreement agreement);
}
