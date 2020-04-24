package ee.ut.dsg.seraph;

import ee.ut.dsg.jasper.operators.s2r.RuntimeManager;
import ee.ut.dsg.jasper.secret.EsperTime;
import ee.ut.dsg.seraph.r2r.R2ROperatorCypher;
import ee.ut.dsg.seraph.s2r.EsperWindowOperatorPGraph;
import ee.ut.dsg.seraph.streams.EPLPGraphStream;
import it.polimi.yasper.core.engine.config.EngineConfiguration;
import it.polimi.yasper.core.enums.Tick;
import it.polimi.yasper.core.format.QueryResultFormatter;
import it.polimi.yasper.core.operators.s2r.syntax.WindowNode;
import it.polimi.yasper.core.querying.ContinuousQueryExecution;
import it.polimi.yasper.core.sds.SDSConfiguration;
import it.polimi.yasper.core.secret.report.ReportImpl;
import org.apache.commons.configuration.ConfigurationException;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestDatabaseManagementServiceBuilder;

import java.util.Observable;

import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

public class KaypherExample {

    static Kaypher sr;

    static {
        try {
            sr = new Kaypher(0, EngineConfiguration.getDefault());
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        TestDatabaseManagementServiceBuilder builder = new TestDatabaseManagementServiceBuilder();
        GraphDatabaseService db = builder.impermanent().build().database(DEFAULT_DATABASE_NAME);
        SDSConfiguration config = null;

        //Streams

        Seraph searph = new Seraph("MATCH (n:Person) RETURN n.name AS name LIMIT 10");

        ContinuousQueryExecution cqe = sr.register(searph, config);

        WindowNode window = new TestWindowNode();

        EsperWindowOperatorPGraph wo = new EsperWindowOperatorPGraph(
                Tick.TIME_DRIVEN,
                new ReportImpl(),
                new EsperTime(RuntimeManager.getEPRuntime(), 0),
                window,
                cqe);


        R2ROperatorCypher r2r = new R2ROperatorCypher(cqe.getContinuousQuery(), cqe.getSDS(), "name", db);
        //Register the query
        PGraphStream writer = new PGraphStream("stream1", null);

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
