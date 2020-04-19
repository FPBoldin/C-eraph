package ee.ut.dsg.seraph.sds;

import ee.ut.dsg.seraph.s2r.EsperWindowAssignerPGraph;
import ee.ut.dsg.seraph.streams.PGraph;
import it.polimi.yasper.core.enums.Maintenance;
import it.polimi.yasper.core.secret.report.Report;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Log4j
@Getter
public class NamedEsperTimeVaryingPGraph extends EsperTimeVaryingPGraphImpl {

    private String uri;

    public NamedEsperTimeVaryingPGraph(String uri, PGraph content, Maintenance maintenance, Report report, EsperWindowAssignerPGraph wo) {
        super(content, maintenance, report, wo);
        this.uri = uri;
    }

    @Override
    public String iri() {
        return uri;
    }

    @Override
    public boolean named() {
        return true;
    }


}