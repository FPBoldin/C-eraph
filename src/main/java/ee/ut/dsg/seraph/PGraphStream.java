package ee.ut.dsg.seraph;

import ee.ut.dsg.seraph.streams.EPLPGraphStream;
import ee.ut.dsg.seraph.streams.PGraph;
import it.polimi.yasper.core.stream.web.WebStreamImpl;

public class PGraphStream extends WebStreamImpl implements Runnable {
    private EPLPGraphStream stream;
    private PGraph pgrah;

    public PGraphStream(String stream_uri, PGraph pgrah) {
        super(stream_uri);
        this.pgrah = pgrah;
    }

    @Override
    public void run() {
        //todo stream.put();
        stream.put(pgrah,1);
    }

    public void setWritable(EPLPGraphStream register) {
        this.stream = register;
    }
}
