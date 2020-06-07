package ee.ut.dsg.seraph.neo4j.poc;


import ee.ut.dsg.seraph.kafka.AvroConsumer;
import ee.ut.dsg.seraph.kafka.AvroProducer;
import ee.ut.dsg.seraph.kafka.SocialNetworkEvent;
import ee.ut.dsg.seraph.neo4j.PGraph;
import ee.ut.dsg.seraph.neo4j.poc.data.PGraphImpl;
import ee.ut.dsg.seraph.neo4j.poc.data.PGraphImplAvro;
import it.polimi.jasper.streams.EPLStream;
import it.polimi.yasper.core.stream.web.WebStreamImpl;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.List;

public class PGraphStream extends WebStreamImpl implements Runnable {
    private EPLStream<PGraph> stream;
    private PGraph pgrah;
    private int consumedTotal;

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
            //stream.put(new PGraphImpl(),System.currentTimeMillis());
            ConsumerRecords<String, SocialNetworkEvent> records = ac.consume();

            records.forEach(event -> {
                System.out.println(event.value().getDate());
                stream.put(new PGraphImplAvro(event.value()), Long.parseLong(event.value().getDate()));
                consumedTotal += 1;
                if (consumedTotal >= 10000){
                    System.out.println(System.currentTimeMillis() + "End of consumption");
                }
            });
            //Thread.sleep(1000);
        }
    }

    public void setWritable(EPLStream<PGraph> register) {
        this.stream = register;
    }
}
