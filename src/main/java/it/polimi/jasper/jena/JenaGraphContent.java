package it.polimi.jasper.jena;

import it.polimi.jasper.secret.content.ContentEventBean;
import lombok.extern.log4j.Log4j;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.GraphUtil;
import org.apache.jena.mem.GraphMem;
import org.apache.jena.reasoner.Reasoner;

@Log4j
public class JenaGraphContent extends ContentEventBean<Graph, Graph> {

    private final Graph graph;

    public JenaGraphContent(Graph graph) {
        this.graph = graph;
    }

    public JenaGraphContent() {
        this.graph = graph();
    }

    private static Graph graph() {
        Reasoner reasoner = ContinuousQueryExecutionFactory.getReasoner();
        return reasoner != null ? reasoner.bind(new GraphMem()) : new GraphMem();
    }

    @Override
    public Graph coalesce() {
        graph.clear();
        elements.forEach(ig -> GraphUtil.addInto(this.graph, ig));
        //        elements.stream().flatMap(ig->GraphUtil.findAll(ig).toList().stream()).forEach(this.graph::add);

        return this.graph;
    }

    public void replace(Graph coalesce) {
        this.graph.clear();
        GraphUtil.addInto(graph, coalesce);
    }
}
