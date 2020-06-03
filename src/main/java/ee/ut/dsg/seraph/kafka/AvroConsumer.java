package ee.ut.dsg.seraph.kafka;

import ee.ut.dsg.seraph.neo4j.poc.data.Event;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.*;

import ee.ut.dsg.seraph.kafka.SocialNetworkEvent;

public class AvroConsumer {

    private KafkaConsumer<String, SocialNetworkEvent> consumer;
    private String topic;

    public AvroConsumer(String topic) {
        this.consumer = new KafkaConsumer<>(getAvroProps());
        this.topic = topic;
    }

    private Properties getAvroProps() {
        Properties props = new Properties();
        // normal consumer
        props.setProperty("bootstrap.servers","localhost:9092");
        props.put("group.id", "socialnetwork-consumer-group");
        props.put("auto.commit.enable", "false");
        props.put("auto.offset.reset", "earliest");

        // avro part (deserializer)
        props.setProperty("key.deserializer", StringDeserializer.class.getName());
        props.setProperty("value.deserializer", KafkaAvroDeserializer.class.getName());
        props.setProperty("schema.registry.url", "http://localhost:8081");
        props.setProperty("specific.avro.reader", "true");
        return props;
    }
    public List<SocialNetworkEvent> consume(){
        consumer.subscribe(Collections.singleton(topic));

        System.out.println("Waiting for data...");

            System.out.println("Polling");
            ConsumerRecords<String, SocialNetworkEvent> records = consumer.poll(100);
            List<SocialNetworkEvent> events = new ArrayList<>();
            records.forEach(event -> events.add(event.value()));

            consumer.commitSync();
            return events;
    }


}
