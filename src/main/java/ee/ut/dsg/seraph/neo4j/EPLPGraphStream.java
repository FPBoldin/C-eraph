package ee.ut.dsg.seraph.neo4j;

import com.espertech.esper.client.EPStatement;
import it.polimi.jasper.streams.EPLStream;
import it.polimi.yasper.core.stream.web.WebStream;
import lombok.Getter;

/**
 * Created by riccardo on 10/07/2017.
 */
@Getter
public class EPLPGraphStream extends EPLStream<PGraph> {

    protected WebStream stream;
    protected EPStatement e;

    public EPLPGraphStream(String uri, WebStream s, EPStatement epl) {
        super(uri, s, epl);
    }

}
