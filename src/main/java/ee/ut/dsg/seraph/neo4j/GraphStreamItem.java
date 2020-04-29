package ee.ut.dsg.seraph.neo4j;

import ee.ut.dsg.seraph.streams.items.RDFStreamItem;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.GraphUtil;

//TODO wrap rid of  JenaGraph
public class GraphStreamItem extends RDFStreamItem<PGraph> {

    private static final long serialVersionUID = 1L;

    public GraphStreamItem(long appTimestamp1, PGraph content1, String stream_uri) {
        super(appTimestamp1, content1, stream_uri);
    }

    @Override
    public PGraph addTo(PGraph abox) {
        return abox;
    }

    @Override
    public PGraph removeFrom(PGraph abox) {
        return abox;
    }

    @Override
    public String toString() {
        return "GraphStreamItem {" + "appTimestamp='" + getAppTimestamp() + '\'' + ", sysTimestamp='" + getSysTimestamp()
                + '\'' + ", content='" + getTypedContent() + '\'' + ", stream_uri='" + getStream_uri() + '\'' + '}';
    }

    @Override
    public String getStreamURI() {
        return getStream_uri();
    }
}
