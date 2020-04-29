package ee.ut.dsg.seraph.neo4j;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.event.map.MapEventBean;

import ee.ut.dsg.seraph.streams.items.StreamItem;
import it.polimi.jasper.secret.content.ContentEventBean;
import it.polimi.yasper.core.secret.content.Content;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.neo4j.graphdb.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class ContentPGraphBean extends ContentEventBean<PGraph, PGraph, PGraph> {

    protected List<PGraph> elements;
    protected PGraph graph;

    protected GraphDatabaseService db;

    @Setter
    private long last_timestamp_changed;

    public ContentPGraphBean() {
        super(null);
        //this.db = db;
        this.elements = new ArrayList<>();

    }

    public void eval(EventBean[] newData, EventBean[] oldData) {
        DStreamUpdate(oldData);
        IStreamUpdate(newData);
    }

    private void handleSingleIStream(StreamItem st) {
        // log.debug("Handling single IStreamTest [" + st + "]");
        elements.add((PGraph) st.getTypedContent());
    }

    private void IStreamUpdate(EventBean[] newData) {
        if (newData != null && newData.length != 0) {
            log.debug("[" + newData.length + "] New Events of type ["
                    + newData[0].getUnderlying().getClass().getSimpleName() + "]");
            for (EventBean e : newData) {
                if (e instanceof MapEventBean) {
                    MapEventBean meb = (MapEventBean) e;
                    if (meb.getProperties() instanceof GraphStreamItem) {
                        handleSingleIStream((StreamItem) e.getUnderlying());
                    } else {
                        for (int i = 0; i < meb.getProperties().size(); i++) {
                            StreamItem st = (StreamItem) meb.get("stream_" + i);
                            handleSingleIStream(st);
                        }
                    }
                }
            }
        }
    }

    protected void DStreamUpdate(EventBean[] oldData) {
        elements.clear();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public void add(PGraph e) {
        elements.add(e);
    }


    public void add(EventBean e) {
        if (e instanceof MapEventBean) {
            MapEventBean meb = (MapEventBean) e;
            if (meb.getUnderlying() instanceof GraphStreamItem) {
                elements.add((PGraph) ((StreamItem) meb.getUnderlying()).getTypedContent());
            } else {
                for (int i = 0; i < meb.getProperties().size(); i++) {
                    StreamItem st = (StreamItem) meb.get("stream_" + i);
                    elements.add((PGraph) st.getTypedContent());
                }
            }
        }
    }

    @Override
    public Long getTimeStampLastUpdate() {
        return last_timestamp_changed;
    }

    @Override
    public PGraph coalesce() {
        Transaction tx = db.beginTx();

        tx.execute("MATCH (n) DETACH DELETE n");

        //TODO First run query (delete n when n.prov == stream(name)) | added the execute delete query
        //TODO create a query that adds all the information into the elements

        /*
            MERGE (p1:Person { name: event.initiated })
            MERGE (p2:Person { name: event.accepted })
            CREATE (p1)-[:FRIENDS { when: event.date }]->(p2)
        */
        elements.forEach(pGraph -> {
            try {
                pGraph.nodes().forEach(node -> {
                    tx.createNode(Label.label("PERSON")).setProperty("name",node);
                });
                pGraph.edges().forEach(edge -> {
                    Node firstNode = tx.findNode(Label.label("PERSON"), "name", edge[0]);
                    Node secondNode = tx.findNode(Label.label("PERSON"), "name", edge[1]);
                    firstNode.createRelationshipTo(secondNode, RelationshipType.withName("FRIENDS")).setProperty(
                    "date", edge[2]);
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        //        elements.stream().flatMap(ig->GraphUtil.findAll(ig).toList().stream()).forEach(this.graph::add);

        tx.commit();

        return this.graph;
    }

    @Override
    public String toString() {
        return elements.toString();
    }

    public EventBean[] asArray() {
        return elements.toArray(new EventBean[size()]);
    }

    public void update(EventBean[] newData, EventBean[] oldData, long event_time) {
        eval(newData, oldData);
        setLast_timestamp_changed(event_time);
    }

    public void replace(PGraph pGraph) {

        Transaction tx = db.beginTx();

        tx.execute("MATCH (n) DETACH DELETE n");

        try {
            pGraph.nodes().forEach(node -> {
                tx.createNode(Label.label("PERSON")).setProperty("name",node);
            });
            pGraph.edges().forEach(edge -> {
                Node firstNode = tx.findNode(Label.label("PERSON"), "name", edge[0]);
                Node secondNode = tx.findNode(Label.label("PERSON"), "name", edge[1]);
                firstNode.createRelationshipTo(secondNode, RelationshipType.withName("FRIENDS"));
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        tx.commit();
    }
}