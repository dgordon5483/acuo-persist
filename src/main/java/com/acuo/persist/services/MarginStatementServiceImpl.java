package com.acuo.persist.services;

import com.acuo.persist.entity.CallStatus;
import com.acuo.persist.entity.MarginStatement;
import com.acuo.persist.spring.Call;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;

@Transactional
public class MarginStatementServiceImpl extends GenericService<MarginStatement> implements MarginStatementService {

    @Override
    public Class<MarginStatement> getEntityType() {
        return MarginStatement.class;
    }

    @Override
    public Iterable<MarginStatement> allStatementsFor(String clientId, CallStatus... statuses) {
        String query = "MATCH (:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m:MarginStatement) " +
                "WITH m " +
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m)<-[*1..2]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                "WHERE step.status IN {statuses} RETURN m, nodes(p), rels(p)";
        return session.query(MarginStatement.class, query, ImmutableMap.of("clientId", clientId, "statuses", statuses));
    }

    @Override
    public Iterable<MarginStatement> allStatementsForClient(String clientId) {
        String query = "MATCH (:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m:MarginStatement) " +
                        "WITH m " +
                        "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-" +
                        "(m) <-[*1..2]-(mc:MarginCall)-[:LAST]->(step:Step) RETURN m, nodes(p), rels(p)";
        return session.query(MarginStatement.class, query, ImmutableMap.of("clientId", clientId));
    }

    @Override
    public MarginStatement statementFor(String marginStatementId, CallStatus... statuses) {
        String query =
                "MATCH p=(f:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-" +
                        "(m:MarginStatement {id:{marginStatementId}})<-[]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                        "WHERE step.status IN {statuses} " +
                        "RETURN m, nodes(p), rels(p)";
        return session.queryForObject(MarginStatement.class, query, ImmutableMap.of("marginStatementId", marginStatementId, "statuses", statuses));
    }

    @Override
    public Iterable<Call> allCallsFor(String clientId, String dateTime) {
        String query =
                "MATCH (:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[r:CLIENT_SIGNS]->(a:Agreement)<-[:STEMS_FROM]-(m:MarginStatement {date:{dateTime}}) " +
                "WITH a, m " +
                "MATCH (m)<-[]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                "WITH {category:a.type, type:mc.callType, status:step.status, balance: mc.balanceAmount, excess: mc.excessAmount} AS Call " +
                "RETURN Call";
        return session.query(Call.class, query, ImmutableMap.of("clientId", clientId, "dateTime", dateTime));
    }

    @Override
    public Iterable<MarginStatement> allStatementsForRecon(String clientId) {
        String query =
                "MATCH (:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m:MarginStatement)<-[]-(mc:MarginCall)-[:LAST]->(step:Step {status:'Unrecon'}) " +
                "WITH m " +
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m)<-[]-(mc:MarginCall {status:'Expected'}) " +
                "RETURN m, mc, nodes(p), rels(p)";
        return session.query(MarginStatement.class, query, ImmutableMap.of("clientId", clientId));
    }

    @Override
    public MarginStatement statementOf(String callId) {
        String query = "MATCH (m:MarginStatement)<-[*1..2]-(mc:MarginCall {id:{callId}}) " +
                "WITH m " +
                "MATCH p=(f:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m)<-[*1..2]-(mc:MarginCall) RETURN m, nodes(p), rels(p)";
        return session.queryForObject(MarginStatement.class, query, ImmutableMap.of("callId", callId));
    }
}