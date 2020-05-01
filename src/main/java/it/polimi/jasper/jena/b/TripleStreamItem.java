package it.polimi.jasper.jena.b;

import it.polimi.jasper.streams.items.StreamItemImpl;
import org.apache.jena.graph.Triple;

//TODO wrap rid of  JenaGraph
public class TripleStreamItem extends StreamItemImpl<Triple> {

    private static final long serialVersionUID = 1L;

    public TripleStreamItem(long appTimestamp1, Triple content1, String stream_uri) {
        super(appTimestamp1, content1, stream_uri);
    }

    @Override
    public String toString() {
        return "GraphStreamItem {" + "appTimestamp='" + getAppTimestamp() + '\'' + ", sysTimestamp='" + getSysTimestamp()
                + '\'' + ", content='" + getTypedContent() + '\'' + ", stream_uri='" + getStream_uri() + '\'' + '}';
    }

    public Triple addTo(Triple abox) {
        this.put(content, abox);
        return getTypedContent();
    }

    public Triple removeFrom(Triple abox) {
        Triple t = getTypedContent();
        if (getTypedContent().equals(abox))
            this.put(content, null);
        return t;
    }

}
