package ee.ut.dsg.seraph.neo4j;


import it.polimi.yasper.core.stream.web.WebStreamImpl;
import lombok.SneakyThrows;

public class PGraphStream extends WebStreamImpl implements Runnable {
    private EPLPGraphStream stream;
    private PGraph pgrah;

    public PGraphStream(String stream_uri, PGraph pgrah) {
        super(stream_uri);
        this.pgrah = pgrah;
    }

    @SneakyThrows
    @Override
    public void run() {
        //todo stream.put();


        while (true){
            stream.put(new PGraphImpl(),System.currentTimeMillis());
            Thread.sleep(5000);
        }




    }

    public void setWritable(EPLPGraphStream register) {
        this.stream = register;
    }
}
