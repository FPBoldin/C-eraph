package ee.ut.dsg.seraph.neo4j;


import it.polimi.jasper.sds.tv.EsperTimeVaryingGeneric;
import it.polimi.jasper.secret.content.ContentEventBean;
import it.polimi.yasper.core.enums.Maintenance;
import it.polimi.yasper.core.operators.s2r.execution.assigner.Assigner;
import it.polimi.yasper.core.sds.SDS;
import it.polimi.yasper.core.secret.report.Report;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Log4j
@Getter
public class NamedEsperTimeVaryingPGraph extends EsperTimeVaryingGeneric<PGraph, PGraph> {

    private String uri;

    public NamedEsperTimeVaryingPGraph(ContentEventBean<PGraph, ?, PGraph> c, String uri, Maintenance maintenance, Report report, Assigner<PGraph, PGraph> wo, SDS<PGraph> sds) {
        super(c, maintenance, report, wo, sds);
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