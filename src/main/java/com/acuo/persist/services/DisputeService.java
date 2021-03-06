package com.acuo.persist.services;

import com.acuo.persist.entity.Dispute;
import com.acuo.persist.ids.MarginStatementId;

public interface DisputeService extends Service<Dispute, String> {

    Dispute relatedTo(MarginStatementId marginStatementId);
}
