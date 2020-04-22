package ee.ut.dsg.seraph.streams;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.sun.jna.StringArray;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public interface PGraph {
    List<String> nodes() throws FileNotFoundException;
    List<String[]> edges() throws FileNotFoundException;
    long timestamp();
}
