package ee.ut.dsg.seraph.neo4j.poc;


import ee.ut.dsg.seraph.kafka.AvroConsumer;
import ee.ut.dsg.seraph.kafka.AvroProducer;
import ee.ut.dsg.seraph.neo4j.PGraph;
import ee.ut.dsg.seraph.neo4j.poc.data.PGraphImpl;
import ee.ut.dsg.seraph.neo4j.poc.data.PGraphImplAvro;
import it.polimi.jasper.streams.EPLStream;
import it.polimi.yasper.core.stream.web.WebStreamImpl;
import lombok.SneakyThrows;

public class PGraphStream extends WebStreamImpl implements Runnable {
    private EPLStream<PGraph> stream;
    private PGraph pgrah;

    public PGraphStream(String stream_uri, PGraph pgrah) {
        super(stream_uri);
        this.pgrah = pgrah;
    }

    @SneakyThrows
    @Override
    public void run() {
        String topic = "sne-avro";
        AvroConsumer ac = new AvroConsumer(topic);

        while (true) {
            ac.consume();
            //stream.put(new PGraphImpl(),System.currentTimeMillis());
            stream.put(new PGraphImplAvro(ac.consume()), System.currentTimeMillis());
            Thread.sleep(1000);
        }
    }

    public void setWritable(EPLStream<PGraph> register) {
        this.stream = register;
    }
}
