package com.acuo.persist.entity;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type="COUNTERPARTY_SIGNS")
@Getter
@Setter
public class CounterpartSignsRelation extends Entity<CounterpartSignsRelation> {

    @StartNode
    private LegalEntity legalEntity;

    @EndNode
    private Agreement agreement;

    private Double initialMarginBalance;

    private Double rounding;

    private Double MTA;

    private Double variationMarginBalance;

    private Double variationPending;

    private Double initialPending;

    private Double initialPendingNonCash;

    private Double variationPendingNonCash;

}
