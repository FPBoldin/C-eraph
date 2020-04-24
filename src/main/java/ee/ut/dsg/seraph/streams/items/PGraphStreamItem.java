package ee.ut.dsg.seraph.streams.items;

import ee.ut.dsg.jasper.streams.items.RDFStreamItem;
import ee.ut.dsg.seraph.streams.PGraph;

import java.io.FileNotFoundException;

//TODO wrap rid of  JenaGraph
public class PGraphStreamItem extends RDFStreamItem<PGraph> {

    private static final long serialVersionUID = 1L;

    public PGraphStreamItem(long appTimestamp1, PGraph content1, String stream_uri) {
        super(appTimestamp1, content1, stream_uri);
    }

    @Override
    public PGraph addTo(PGraph abox) throws FileNotFoundException {
        return null;
    }

    @Override
    public PGraph removeFrom(PGraph abox) {
        return null;
    }

    /*
    public PGraph addTo(PGraph abox) throws FileNotFoundException {
        PGraph typedContent = this.getTypedContent();
        typedContent.nodes().addAll(abox.edges());
        typedContent.nodes().addAll(abox.edges());
        return abox;
    }

    public PGraph removeFrom(PGraph abox) {
        PGraph typedContent = this.getTypedContent();
        typedContent.edges().removeAll(abox.edges());
        typedContent.nodes().removeAll(abox.nodes());
        return abox;
    }
     */

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
