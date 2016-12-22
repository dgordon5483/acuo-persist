package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.CurrencyConverter;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.opengamma.strata.basics.currency.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;
import org.neo4j.ogm.annotation.typeconversion.DateString;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class MarginStatement extends Entity{

    private Double totalAmount;

    private String legalEntityId;

    private String direction;

    @Convert(LocalDateConverter.class)
    private LocalDate date;

    private Double interestPayment;

    private Double productCashFlows;

    private Double feesCommissions;

    private Double pendingCash;

    private Double PAI;

    private Double pendingNonCash;

    @Property(name="id")
    private String marginStatementId;

    private String status;

    @Convert(CurrencyConverter.class)
    private Currency currency;

    @Relationship(type = "STEMS_FROM")
    private Agreement agreement;

    @Relationship(type = "IS_RECEIVED_IN", direction = Relationship.INCOMING)
    private Set<MarginCall> receviedMarginCalls = new HashSet<>();

    @Relationship(type = "IS_EXPECTED_IN", direction = Relationship.INCOMING)
    private Set<MarginCall> expectedMarginCalls = new HashSet<>();

}
