package ee.ut.dsg.seraph.sds;

import ee.ut.dsg.seraph.s2r.EsperWindowAssignerPGraph;
import ee.ut.dsg.seraph.streams.PGraph;
import it.polimi.yasper.core.enums.Maintenance;
import it.polimi.yasper.core.secret.report.Report;
import lombok.extern.log4j.Log4j;

@Log4j
public class EsperTimeVaryingPGraphImpl extends EsperTimeVaryingPGraph {

    public EsperTimeVaryingPGraphImpl(PGraph content, Maintenance maintenance, Report report, EsperWindowAssignerPGraph wo) {
        super(content, maintenance, report, wo);
    }


}