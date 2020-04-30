package ee.ut.dsg.seraph.neo4j;

import ee.ut.dsg.seraph.engine.esper.EsperRSPEngine;
import it.polimi.yasper.core.engine.config.EngineConfiguration;
import it.polimi.yasper.core.engine.features.QueryObserverRegistrationFeature;
import it.polimi.yasper.core.engine.features.QueryRegistrationFeature;
import it.polimi.yasper.core.engine.features.QueryStringRegistrationFeature;
import it.polimi.yasper.core.format.QueryResultFormatter;
import it.polimi.yasper.core.operators.r2r.RelationToRelationOperator;
import it.polimi.yasper.core.operators.r2s.RelationToStreamOperator;
import it.polimi.yasper.core.operators.s2r.StreamToRelationOperator;
import it.polimi.yasper.core.querying.ContinuousQuery;
import it.polimi.yasper.core.querying.ContinuousQueryExecution;
import it.polimi.yasper.core.sds.SDS;
import it.polimi.yasper.core.sds.SDSConfiguration;
import it.polimi.yasper.core.stream.data.WebDataStream;

public class Kaypher extends EsperRSPEngine<PGraph> implements QueryObserverRegistrationFeature, QueryRegistrationFeature<Seraph>, QueryStringRegistrationFeature {

    public Kaypher(long t0, EngineConfiguration configuration) {
        super(t0, configuration);
        stream_registration_service = new EsperStreamRegistrationServiceImplNeo4j(admin);
    }

    @Override
    public void register(ContinuousQuery q, QueryResultFormatter f) {

    }

    @Override
    public ContinuousQueryExecution register(Seraph seraph) {
        return null;
    }

    @Override
    public ContinuousQueryExecution register(Seraph seraph, SDSConfiguration sdsConfiguration) {

        new Neo4jContinuousQueryExecution();
        return cqe;
    }

    @Override
    public ContinuousQueryExecution register(String s) {
        return null;
    }

    @Override
    public ContinuousQueryExecution register(String s, SDSConfiguration sdsConfiguration) {
        return null;
    }
}