package ee.ut.dsg.seraph.neo4j;

import it.polimi.jasper.operators.SolutionMappingImpl;
import it.polimi.yasper.core.operators.r2r.RelationToRelationOperator;
import it.polimi.yasper.core.querying.ContinuousQuery;
import it.polimi.yasper.core.querying.result.SolutionMapping;
import it.polimi.yasper.core.sds.SDS;
import lombok.extern.log4j.Log4j;
import org.apache.jena.query.QueryExecution;
import org.neo4j.cypher.internal.CommunityCompilerFactory;
import org.neo4j.cypher.internal.javacompat.ExecutionEngine;
import org.neo4j.cypher.internal.javacompat.GraphDatabaseCypherService;
import org.neo4j.cypher.internal.v4_0.parser.CypherParser;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.impl.factory.GraphDatabaseFacade;
import org.neo4j.logging.NullLogProvider;
import org.neo4j.logging.internal.DatabaseLogContext;
import org.neo4j.logging.internal.DatabaseLogProvider;
import org.neo4j.monitoring.Monitors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Log4j
public class R2ROperatorCypher implements RelationToRelationOperator<Map<String, Object>> {

    private final ContinuousQuery query;
    private final SDS<PGraph> sds;
    private final String baseURI;
    public final List<String> resultVars;
    private QueryExecution execution;

    GraphDatabaseService db;


    private Transaction tx;

    public R2ROperatorCypher(ContinuousQuery query, SDS<PGraph> sds, String baseURI, GraphDatabaseService db) {
        this.db = db;
        this.query = query;
        this.sds = sds;
        this.baseURI = baseURI;
        resultVars = query.getResultVars();

    }

    @Override
    public Stream<SolutionMapping<Map<String, Object>>> eval(long ts) {
        //TODO fix up to stream
        String id = baseURI + "result;" + ts;
        this.tx = db.beginTx();

        Result result = tx.execute(query.getSPARQL());
//        |--name-|--age--|-email--|
//        |--Fred--|--22--|--null--|
//        |--Riccardo--|--29--|--null--|

        List<Map<String, Object>> res = new ArrayList<>();
        while (result.hasNext()) {
            Map<String, Object> next = result.next();
            res.add(next);
//        |name-->Fred
//        |age-->22
        }

        tx.commit();
        tx.close();

        return res.stream().map(b -> new SolutionMappingImpl<>(id, b, this.resultVars, ts));
    }

}
