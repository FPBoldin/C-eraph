package ee.ut.dsg.seraph.kafka;

import com.github.javafaker.Faker;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.*;
import ee.ut.dsg.seraph.kafka.SocialNetworkEvent;
import org.stringtemplate.v4.ST;

import java.time.LocalDateTime;
import java.util.Properties;

public class AvroProducer {

    private Producer<String, SocialNetworkEvent> producer;
    private String topic;
    private long timeCounter;
    private long nameCounter;

    public AvroProducer(String topic) {
        this.producer = new KafkaProducer<>(getAvroProps());
        this.topic = topic;
    }

    private Properties getAvroProps() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("schema.registry.url", "http://localhost:8081");
        props.put("key.serializer", KafkaAvroSerializer.class.getName());
        props.put("value.serializer", KafkaAvroSerializer.class.getName());
        return props;
    }

    private String generatePersonName() {
        //Faker faker = new Faker();
        //String name = faker.name().firstName();
        nameCounter += 1;
        String name = String.valueOf(nameCounter);
        return name;
    }

    private String generateDate() {
        timeCounter += 1;
        String datetime = String.valueOf(timeCounter);
        return datetime;
    }

    public void produce() {
        SocialNetworkEvent sne = SocialNetworkEvent.newBuilder()
                .setAccepted("Fred")
                .setInitiated(generatePersonName())
                .setFriends(true)
                .setDate(generateDate())
                .build();
        ProducerRecord<String, SocialNetworkEvent> producerRecord = new ProducerRecord<>(topic,"X" , sne);
        producer.send(producerRecord);
    }
}
