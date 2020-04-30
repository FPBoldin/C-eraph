package ee.ut.dsg.seraph.neo4j;

import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.SafeIterator;
import it.polimi.jasper.operators.s2r.AbstractEsperWindowAssigner;
import it.polimi.jasper.operators.s2r.epl.EPLFactory;
import it.polimi.jasper.sds.tv.EsperTimeVaryingGeneric;
import it.polimi.jasper.utils.EncodingUtils;
import it.polimi.yasper.core.enums.Maintenance;
import it.polimi.yasper.core.enums.ReportGrain;
import it.polimi.yasper.core.enums.Tick;
import it.polimi.yasper.core.operators.s2r.StreamToRelationOperator;
import it.polimi.yasper.core.operators.s2r.execution.instance.Window;
import it.polimi.yasper.core.operators.s2r.syntax.WindowNode;
import it.polimi.yasper.core.sds.SDS;
import it.polimi.yasper.core.sds.timevarying.TimeVarying;
import it.polimi.yasper.core.secret.content.Content;
import it.polimi.yasper.core.secret.report.Report;
import it.polimi.yasper.core.secret.time.Time;
import it.polimi.yasper.core.stream.data.WebDataStream;
import lombok.RequiredArgsConstructor;
import org.neo4j.graphdb.GraphDatabaseService;


import java.util.List;
import java.util.Observable;

@RequiredArgsConstructor
public class EsperGGWindowOperator implements StreamToRelationOperator<PGraph, PGraph> {

    private final Tick tick;
    private final Report report;
    private final Boolean eventtime;
    private final ReportGrain reportGrain;
    private final Maintenance maintenance;
    private final Time time;
    private final WindowNode wo;
    private final SDS<PGraph> context;
    private final GraphDatabaseService db;

    @Override
    public String iri() {
        return wo.iri();
    }

    @Override
    public boolean named() {
        return wo.named();
    }

    @Override
    public TimeVarying<PGraph> apply(WebDataStream<PGraph> s) {
        EsperGGWindowAssigner consumer = new EsperGGWindowAssigner(s.getURI());
        s.addConsumer(consumer);
        return consumer.set(context);
    }

    class EsperGGWindowAssigner extends AbstractEsperWindowAssigner<PGraph, PGraph> {

        public EsperGGWindowAssigner(String name) {
            super(EncodingUtils.encode(name),
                    EsperGGWindowOperator.this.tick,
                    EsperGGWindowOperator.this.report,
                    EsperGGWindowOperator.this.eventtime,
                    EPLFactory.getWindowAssigner(EsperGGWindowOperator.this.tick,
                            EsperGGWindowOperator.this.maintenance,
                            EsperGGWindowOperator.this.report,
                            EsperGGWindowOperator.this.eventtime,
                            name, wo.getStep(), wo.getRange(), wo.getUnitStep(), wo.getUnitRange(), wo.getType(),
                            EsperGGWindowOperator.this.time)

                    , EsperGGWindowOperator.this.time);
        }

        public Content<PGraph, PGraph> getContent(long now) {
            SafeIterator<EventBean> iterator = statement.safeIterator();
            ContentPGraphBean events = new ContentPGraphBean(db);
            events.setLast_timestamp_changed(now);
            while (iterator.hasNext()) {
                events.add(iterator.next());
            }
            return events;
        }

        @Override
        public List<Content<PGraph, PGraph>> getContents(long now) {
            return null;
        }


        @Override
        public TimeVarying<PGraph> set(SDS<PGraph> sds) {
            EsperTimeVaryingGeneric<PGraph, PGraph> n = named()
                    ? new NamedEsperTimeVaryingPGraph(new ContentPGraphBean(db), name, EsperGGWindowOperator.this.maintenance, report, this, sds)
                    : new EsperTimeVaryingPGraphImpl(new ContentPGraphBean(db), EsperGGWindowOperator.this.maintenance, report, this, sds);
            statement.addListener(n);
            return n;
        }

        @Override
        public void notify(PGraph arg, long ts) {
            process(arg, ts);
        }

        public boolean process(PGraph g, long now) {

            long appTime = time.getAppTime();

            if (appTime < now) {
                time.setAppTime(now);
                runtime.sendEvent(new GraphStreamItem(now, g, name), name);
                return true;
            } else if (appTime == now) {
                runtime.sendEvent(new GraphStreamItem(now, g, name), name);
                return true;
            } else
                return false;

        }

        @Override
        public void update(Observable o, Object arg) {
            PGraphStreamItem arg1 = (PGraphStreamItem) arg;
            process(arg1.getTypedContent(), eventtime ? arg1.getAppTimestamp() : arg1.getSysTimestamp());
        }

        @Override
        public Content<PGraph, PGraph> compute(long l, Window window) {
            return null;
            //TODO
        }

    }
}
