package graphTool;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Relationship;

public interface DatabaseBuilder {
    Label getLabel(Relationship rel);
}
