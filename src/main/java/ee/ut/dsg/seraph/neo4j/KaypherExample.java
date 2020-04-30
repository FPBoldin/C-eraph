package ee.ut.dsg.seraph.neo4j;


import it.polimi.jasper.operators.r2s.JIStream;
import it.polimi.jasper.operators.r2s.JRStream;
import it.polimi.jasper.operators.s2r.epl.RuntimeManager;
import it.polimi.jasper.secret.EsperTime;
import it.polimi.yasper.core.engine.config.EngineConfiguration;
import it.polimi.yasper.core.enums.Maintenance;
import it.polimi.yasper.core.enums.ReportGrain;
import it.polimi.yasper.core.enums.Tick;
import it.polimi.yasper.core.format.QueryResultFormatter;
import it.polimi.yasper.core.operators.r2r.RelationToRelationOperator;
import it.polimi.yasper.core.operators.r2s.RelationToStreamOperator;
import it.polimi.yasper.core.operators.s2r.syntax.WindowNode;
import it.polimi.yasper.core.querying.ContinuousQueryExecution;
import it.polimi.yasper.core.sds.SDSConfiguration;
import it.polimi.yasper.core.sds.timevarying.TimeVarying;
import it.polimi.yasper.core.secret.report.ReportImpl;
import it.polimi.yasper.core.stream.data.DataStreamImpl;
import org.apache.commons.configuration.ConfigurationException;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestDatabaseManagementServiceBuilder;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

public class KaypherExample {

    static Kaypher sr;

    public static EngineConfiguration aDefault;


    public static void main(String[] args) {

        TestDatabaseManagementServiceBuilder builder = new TestDatabaseManagementServiceBuilder();
        GraphDatabaseService db = builder.impermanent().build().database(DEFAULT_DATABASE_NAME);
        URL resource = KaypherExample.class.getResource("/kaypher.properties");

        try {
            EngineConfiguration en = new EngineConfiguration("/home/fred/IdeaProjects/C-eraph-v2/src/main/resources/kaypher.properties");
            //EngineConfiguration en = new EngineConfiguration("/Users/riccardo/_Projects/ceraph/src/main/resources/kaypher.properties");
            sr = new Kaypher(0, en);
            SDSConfiguration config = new SDSConfiguration(resource.getPath());

            //Streams

            Seraph searph = new Seraph("MATCH (n) RETURN n");

            Neo4jSDS sds = new Neo4jSDS();

            WindowNode window = new TestWindowNode();
            EsperGGWindowOperator wo = new EsperGGWindowOperator(
                    Tick.TIME_DRIVEN,
                    new ReportImpl(),
                    true,
                    ReportGrain.MULTIPLE,
                    Maintenance.INCREMENTAL,
                    new EsperTime(RuntimeManager.getEPRuntime(), 0),
                    window,
                    sds,
                    db);

            PGraphStream writer = new PGraphStream("stream1", null);

            ContinuousQueryExecution cqe =   new Neo4jContinuousQueryExecution(
                    new DataStreamImpl("stream1"),
                    searph,
                    sds,
                    new R2ROperatorCypher(searph , sds ,"SocialNetwork", db),
                    new JRStream<PBinding>(),
                    wo);

            EPLPGraphStream register = (EPLPGraphStream) sr.register(writer);

            TimeVarying<PGraph> apply = wo.apply(register);

            sds.add(apply);

            writer.setWritable(register);

            sds.addObserver((Observer) cqe);

            cqe.add(new QueryResultFormatter("Neo4j", true) {
                @Override
                public void update(Observable o, Object arg) {
                            System.out.println(arg);
                }
            });

            //In real application we do not have to start the stream.
            (new Thread(writer)).start();

        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }
}
