package ee.ut.dsg.seraph.neo4j;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;

import it.polimi.jasper.operators.s2r.AbstractEsperWindowAssigner;
import it.polimi.yasper.core.enums.Maintenance;
import it.polimi.yasper.core.querying.ContinuousQueryExecution;
import it.polimi.yasper.core.sds.timevarying.TimeVarying;
import it.polimi.yasper.core.secret.report.Report;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by riccardo on 05/07/2017.
 */
@Log4j
@Getter
public abstract class EsperTimeVaryingPGraph extends Observable implements StatementAwareUpdateListener, TimeVarying<PGraph> {

    protected Report report;
    protected AbstractEsperWindowAssigner wa;
    protected Maintenance maintenance;
    protected long now;

    protected ContentPGraphBean c;

    public EsperTimeVaryingPGraph(PGraph content, Maintenance maintenance, Report report, AbstractEsperWindowAssigner wa) {
        this.maintenance = maintenance;
        this.wa = wa;
        this.report = report;
        this.c = new ContentPGraphBean();

    }

    @Override
    public synchronized void update(EventBean[] newData, EventBean[] oldData, EPStatement stmt, EPServiceProvider eps) {

        if (!wa.getStatement().equals(stmt))
            throw new RuntimeException("Different Update Statement");

        long event_time = eps.getEPRuntime().getCurrentTime();

        long systime = System.currentTimeMillis();

        this.c.update(newData, oldData, event_time);

        if (report.report(null, c, event_time, systime)) {
            log.debug("[" + Thread.currentThread() + "][" + systime + "] FROM STATEMENT: " + stmt.getText() + " AT "
                    + event_time);

            setChanged();
            notifyObservers(event_time);
        }

    }

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
    }

    @Override
    public void materialize(long ts) {
        if (this.c.getTimeStampLastUpdate() < ts) {
            this.c.replace((PGraph) this.wa.getContent(ts).coalesce());
        } else
            this.c.coalesce();
    }

    @Override
    public PGraph get() {
        return c.coalesce();
    }

    @Override
    public String iri() {
        return "";
    }

    @Override
    public boolean named() {
        return false;
    }

    public void addListener(ContinuousQueryExecution cqe) {
        this.addObserver((Observer) cqe);
    }

    public void removeListener(ContinuousQueryExecution cqe) {
        this.deleteObserver((Observer) cqe);
    }

}
