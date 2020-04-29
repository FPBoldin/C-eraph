package ee.ut.dsg.seraph.neo4j;


import ee.ut.dsg.seraph.streams.schema.RDFStreamSchema;
import it.polimi.yasper.core.stream.metadata.SchemaEntry;

import java.util.HashSet;
import java.util.Set;

public class PGraphStreamSchema extends RDFStreamSchema {

    public PGraphStreamSchema() {
        super(PGraph.class);
    }


    @Override
    public Set<SchemaEntry> entrySet() {
        return new HashSet<>();
    }

    @Override
    public boolean validate(Object o) {
        return true;
    }
}

