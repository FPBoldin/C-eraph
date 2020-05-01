package it.polimi.jasper.jena;

import it.polimi.jasper.streams.items.StreamItemImpl;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.GraphUtil;

//TODO wrap rid of  JenaGraph
public class GraphStreamItem extends StreamItemImpl<Graph> {

    private static final long serialVersionUID = 1L;

    public GraphStreamItem(long appTimestamp1, Graph content1, String stream_uri) {
        super(appTimestamp1, content1, stream_uri);
    }

    public Graph addTo(Graph abox) {
        GraphUtil.addInto(abox, this.getTypedContent());
        return abox;
    }

    public Graph removeFrom(Graph abox) {
        GraphUtil.deleteFrom(abox, getTypedContent());
        return abox;
    }

    @Override
    public String toString() {
        return "GraphStreamItem {" + "appTimestamp='" + getAppTimestamp() + '\'' + ", sysTimestamp='" + getSysTimestamp()
                + '\'' + ", content='" + getTypedContent() + '\'' + ", stream_uri='" + getStream_uri() + '\'' + '}';
    }

}
