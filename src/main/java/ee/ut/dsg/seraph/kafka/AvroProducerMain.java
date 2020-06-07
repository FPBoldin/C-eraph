package ee.ut.dsg.seraph.kafka;

import ee.ut.dsg.seraph.neo4j.poc.data.PGraphImpl;

public class AvroProducerMain {
    public static void main(String[] args) throws InterruptedException {
        String topic = "sne-avro";
        AvroProducer ap = new AvroProducer(topic);

        for (int i = 0; i < 10000; i++) {
            ap.produce();
        }
        System.out.println("Exiting");
        System.exit(0);

/*
        while (true) {
            ap.produce();
            //stream.put(new PGraphImpl(),System.currentTimeMillis());
            Thread.sleep(0);
        }
  */
    }

}
