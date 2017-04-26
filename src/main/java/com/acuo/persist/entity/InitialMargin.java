package com.acuo.persist.entity;

import com.acuo.persist.entity.enums.Side;
import com.opengamma.strata.basics.currency.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;

import java.time.LocalDate;
import java.util.Map;

import static com.acuo.common.model.margin.Types.MarginType.Initial;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = true)
public class InitialMargin extends MarginCall<InitialMargin> {

    private Double exchangeRequirement;
    private Double brokerRequirement;
    private Double initialBalanceCash;
    private Double initialBalanceNonCash;
    private String IMRole;

    public InitialMargin() {}

    public InitialMargin(Side side, Double value, LocalDate valuationDate, LocalDate callDate, Currency currency, Agreement agreement, Map<Currency, Double> rates) {
        super(side, value, valuationDate, callDate, currency, agreement, rates);
        this.marginType = Initial;
        this.itemId = marginCallId(side, agreement, callDate, Initial);
    }
}
