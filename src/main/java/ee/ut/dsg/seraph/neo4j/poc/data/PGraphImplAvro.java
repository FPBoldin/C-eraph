package ee.ut.dsg.seraph.neo4j.poc.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ee.ut.dsg.seraph.kafka.SocialNetworkEvent;
import ee.ut.dsg.seraph.neo4j.PGraph;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;

public class PGraphImplAvro implements PGraph {
    List<SocialNetworkEvent> events;
    public PGraphImplAvro(List<SocialNetworkEvent> e) {
        this.events = e;
        // {"initiated": "Cory", "accepted": "Levi", "friends": true, "date": "2019-08-08T16:13:11.774754"}
    }

    public List<String> nodes() throws FileNotFoundException {
        Set<String> s = new HashSet<>();
        List<String> personNames = new ArrayList<>();
        events.forEach(event -> {
            s.add(event.getAccepted());
            s.add(event.getInitiated());
        });
        personNames.addAll(s);
        return personNames; // ["Cory","Zidane","Kaka"...]
    }


    public List<String[]> edges() throws FileNotFoundException {
        //String[] strings = {"Cory", "Levi", "friends"};
        List<String[]> strings = new ArrayList<String[]>();

        events.forEach(event -> {
            strings.add(new String[]{event.getInitiated(), event.getAccepted(), "friends", event.getDate()});
        });

        //List<String[]> strings1 = Arrays.asList(new String[][]{strings});
        return strings; // [{"Cory","Levi","friends","2019-05-02"}, ...]
    }


    public long timestamp() {
        return 0;
    }
}
