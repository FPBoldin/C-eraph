package ee.ut.dsg.seraph.s2r;

import ee.ut.dsg.seraph.streams.PGraph;
import ee.ut.dsg.jasper.operators.s2r.EPLFactory;
import it.polimi.yasper.core.enums.Tick;
import it.polimi.yasper.core.operators.s2r.StreamToRelationOperator;
import it.polimi.yasper.core.operators.s2r.execution.assigner.Assigner;
import it.polimi.yasper.core.operators.s2r.syntax.WindowNode;
import it.polimi.yasper.core.querying.ContinuousQueryExecution;
import it.polimi.yasper.core.sds.timevarying.TimeVarying;
import it.polimi.yasper.core.secret.report.Report;
import it.polimi.yasper.core.secret.time.Time;
import it.polimi.yasper.core.stream.data.WebDataStream;
import lombok.RequiredArgsConstructor;
import org.neo4j.graphdb.GraphDatabaseService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class EsperWindowOperatorPGraph implements StreamToRelationOperator<PGraph, PGraph> {

    private final Tick tick;
    private final Report report;
    private final Time time;
    private final WindowNode wo;
    private final ContinuousQueryExecution context;

    private final List<Assigner> assigners = new ArrayList();

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
        EsperWindowAssignerPGraph windowAssigner = EPLFactory.getWindowAssignerPGraph(tick, report, true, s.getURI(), wo.getStep(), wo.getRange(), wo.getUnitStep(), wo.getUnitRange(), wo.getType(), time);
        s.addConsumer(windowAssigner);
        this.assigners.add(windowAssigner);
        return windowAssigner.set(context);
    }

}
