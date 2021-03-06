package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
@Data
@ToString(callSuper = true, exclude = {"values"})
@EqualsAndHashCode(callSuper = true, exclude = {"values"})
public class TradeValuation extends Valuation {

    @Relationship(type = "VALUE")
    private Set<TradeValueRelation> values;
}


