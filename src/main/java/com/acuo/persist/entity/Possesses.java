package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = "POSSESSES")
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"client","asset"})
@ToString(exclude = {"client","asset"})
public class Possesses extends Entity<Possesses>{

    @StartNode
    Client client;

    @EndNode
    Asset asset;

    private Double quantities;
    private Double availableQuantities;
    private Double earmarkedQuantities;
    private Double deployedQuantities;
    private Double departedQuantities;
    private Double arrivingQuantities;
    private Double internalCost;
    private Double opptCost;

}