package com.acuo.persist.services;

import com.acuo.persist.entity.Agreement;
import com.acuo.persist.ids.PortfolioId;

public interface AgreementService extends Service<Agreement, String> {

    Agreement agreementFor(PortfolioId portfolioId);
}
