package ee.ut.dsg.seraph.streams;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ee.ut.dsg.seraph.streams.items.Event;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PGraphImpl implements PGraph {

    // {"initiated": "Cory", "accepted": "Levi", "friends": true, "date": "2019-08-08T16:13:11.774754"}

    @Override
    public List<String> nodes() throws FileNotFoundException {
        ArrayList<String> personNames = null;
        URL url = getClass().getResource("SocialNetwork");
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<Event>>(){}.getType();
        Collection<Event> events = gson.fromJson(new FileReader(url.getPath()), collectionType);
        for (Event event:
             events) {
            if (!personNames.contains(event.getAccepted())){ // avoiding duplicate nodes
                personNames.add(event.getAccepted());
            }
            if (!personNames.contains(event.getInitiated())){
                personNames.add(event.getInitiated());
            }
        }

        return personNames; // ["Cory","Zidane","Kaka"...]
    }

    @Override
    public List<String[]> edges() throws FileNotFoundException {
        //String[] strings = {"Cory", "Levi", "friends"};
        List<String[]> strings = null;
        URL url = getClass().getResource("SocialNetwork");
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<Event>>(){}.getType();
        Collection<Event> events = gson.fromJson(new FileReader(url.getPath()), collectionType);
        for (Event event:
                events) {
            strings.add(new String[]{event.getInitiated(), event.getAccepted(), "friends", event.getDate()});
        }

        //List<String[]> strings1 = Arrays.asList(new String[][]{strings});
        return strings; // [{"Cory","Levi","friends","2019-05-02"}, ...]
    }

    @Override
    public long timestamp() {
        return 0;
    }
}
