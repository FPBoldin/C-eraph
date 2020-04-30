package ee.ut.dsg.seraph.neo4j;


import it.polimi.jasper.operators.s2r.epl.RuntimeManager;
import it.polimi.jasper.secret.EsperTime;
import it.polimi.yasper.core.engine.config.EngineConfiguration;
import it.polimi.yasper.core.enums.Maintenance;
import it.polimi.yasper.core.enums.ReportGrain;
import it.polimi.yasper.core.enums.Tick;
import it.polimi.yasper.core.format.QueryResultFormatter;
import it.polimi.yasper.core.operators.s2r.syntax.WindowNode;
import it.polimi.yasper.core.querying.ContinuousQueryExecution;
import it.polimi.yasper.core.sds.SDSConfiguration;
import it.polimi.yasper.core.sds.timevarying.TimeVarying;
import it.polimi.yasper.core.secret.report.ReportImpl;
import org.apache.commons.configuration.ConfigurationException;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestDatabaseManagementServiceBuilder;

import java.net.URL;
import java.util.Observable;

import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

public class KaypherExample {

    static Kaypher sr;

    public static EngineConfiguration aDefault;


    public static void main(String[] args) {

        TestDatabaseManagementServiceBuilder builder = new TestDatabaseManagementServiceBuilder();
        GraphDatabaseService db = builder.impermanent().build().database(DEFAULT_DATABASE_NAME);
        URL resource = KaypherExample.class.getResource("/kaypher.properties");
//        EngineConfiguration en = new EngineConfiguration("/home/fred/IdeaProjects/C-eraph-testing/src/main/resources/kaypher.properties");
        try {
            EngineConfiguration en = new EngineConfiguration("/Users/riccardo/_Projects/ceraph/src/main/resources/kaypher.properties");
            sr = new Kaypher(0, en);
            SDSConfiguration config = new SDSConfiguration(resource.getPath());

            //Streams

            Seraph searph = new Seraph("MATCH (n:Person) RETURN n.name AS name LIMIT 10");


            ContinuousQueryExecution cqe = sr.register(searph, config);

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
                    sds);


                /*
                Tick.TIME_DRIVEN,
                new ReportImpl(),
                new EsperTime(RuntimeManager.getEPRuntime(), 0),
                window,
                cqe);
*/
            //R2ROperatorCypher r2r = new R2ROperatorCypher(cqe.getContinuousQuery(), cqe.getSDS(), "name", db);

            //Register the query
            PGraphStream writer = new PGraphStream("stream1", null);

            EPLPGraphStream register = (EPLPGraphStream) sr.register(writer);

            TimeVarying<PGraph> apply = wo.apply(register);

            //TODO just create the SDS and add the apply to the SDS
            /**
             * The idea of the CQE is that it takes the data when it is notified by Esper
             * CQE applies the R2R operator (Seraph), then for each result of Seraph applies the r2s operator
             * just copy r2s for PGraphs.
             * For my example the Engine is only needed for the communication with EPL
             */

            writer.setWritable(register);

            cqe.add(new QueryResultFormatter("Neo4j", true) {
                @Override
                public void update(Observable o, Object arg) {


                }
            });

            //In real application we do not have to start the stream.
            (new Thread(writer)).start();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }
}
