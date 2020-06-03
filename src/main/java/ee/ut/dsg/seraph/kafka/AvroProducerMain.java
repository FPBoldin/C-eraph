package ee.ut.dsg.seraph.kafka;

import ee.ut.dsg.seraph.neo4j.poc.data.PGraphImpl;

public class AvroProducerMain {
    public static void main(String[] args) throws InterruptedException {
        String topic = "sne-avro";
        AvroProducer ap = new AvroProducer(topic);

        while (true) {
            ap.produce();
            //stream.put(new PGraphImpl(),System.currentTimeMillis());
            Thread.sleep(50);
        }
    }

}
