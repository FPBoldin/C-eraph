package ee.ut.dsg.seraph;

import ee.ut.dsg.seraph.r2r.R2ROperatorCypher;
import ee.ut.dsg.seraph.streams.EPLPGraphStream;
import ee.ut.dsg.seraph.streams.PGraph;
import it.polimi.yasper.core.format.QueryResultFormatter;
import it.polimi.yasper.core.querying.ContinuousQueryExecution;
import it.polimi.yasper.core.sds.SDSConfiguration;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestDatabaseManagementServiceBuilder;

import java.util.Observable;

import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

public class KaypherExample {

    static Kaypher sr = new Kaypher(0, null);

    public static void main(String[] args) {

        TestDatabaseManagementServiceBuilder builder = new TestDatabaseManagementServiceBuilder();
        GraphDatabaseService db = builder.impermanent().build().database(DEFAULT_DATABASE_NAME);

        SDSConfiguration config = null;

        //Streams

        Seraph searph = new Seraph("MATCH (n:Person) RETURN n.name AS name LIMIT 10");

        ContinuousQueryExecution cqe = sr.register(searph, config);

        R2ROperatorCypher r2r = new R2ROperatorCypher(cqe.getContinuousQuery(),cqe.getSDS(), "name", db);

        //Register the query
        PGraphStream writer = new PGraphStream("name", );

        EPLPGraphStream register = sr.register(writer);

        writer.setWritable(register);


        cqe.add(new QueryResultFormatter("Neo4j", true) {
            @Override
            public void update(Observable o, Object arg) {




            }
        });

        //In real application we do not have to start the stream.
        (new Thread(writer)).start();

    }
}
