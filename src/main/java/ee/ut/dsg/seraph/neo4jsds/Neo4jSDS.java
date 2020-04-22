package ee.ut.dsg.seraph.neo4jsds;

import ee.ut.dsg.seraph.content.ContentPGraphBean;
import ee.ut.dsg.seraph.streams.PGraph;
import ee.ut.dsg.seraph.streams.items.ExternalNode;
import it.polimi.yasper.core.sds.SDS;
import it.polimi.yasper.core.sds.timevarying.TimeVarying;
import it.polimi.yasper.core.secret.time.Time;
import it.polimi.yasper.core.secret.time.TimeFactory;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.apache.commons.rdf.api.IRI;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.compose.MultiUnion;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.impl.InfModelImpl;
import org.apache.jena.rdf.model.impl.ModelCom;
import org.apache.jena.reasoner.InfGraph;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.sparql.core.DatasetGraphFactory;
import org.apache.jena.sparql.core.DatasetImpl;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestDatabaseManagementServiceBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

@Log4j
public class Neo4jSDS extends TestDatabaseManagementServiceBuilder implements SDS<PGraph> {

    private Time time = TimeFactory.getInstance();

    private List<TimeVarying<PGraph>> tvgs = new ArrayList<>();


    @Override
    public Collection<TimeVarying<PGraph>> asTimeVaryingEs() {
        return null;
    }

    @Override
    public void add(IRI iri, TimeVarying<PGraph> tvg) {
        tvgs.add(tvg);

    }

    @Override
    public void add(TimeVarying<PGraph> tvg) {

    }

    @Override
    public void materialize(long ts) {

    }
}