package graphTool;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

import java.util.Arrays;
import java.util.List;

/**
 * Const holds variables used throughout GGIT/src/main/graphTool
 */
public class Const {
    /**
     * Node Properties
     */
    public static final String UUID = "id";

    public static final String LONGITUDE = "longitude";

    public static final String LATITUDE = "latitude";

    public static final String NAME = "name";

    public static final String DESCRIPTION = "description";

    public static List<String> NODE_PROPERTIES = Arrays.asList( UUID, NAME, LATITUDE, LONGITUDE, DESCRIPTION );

    /**
     * Database Labels
     */
    public static final Label ROOT_LABEL = Label.label("ROOT");

    public static final Label OBSERVATION_LABEL = Label.label("OBSERVATION");

    public static final Label KNOWLEDGE_LABEL = Label.label("KNOWLEDGE");

    /**
     * Database Relationships
     */
    public static final RelationshipType RELATE_ROOT_OBSERVATION = RelationshipType.withName("Observation");

    public static final RelationshipType RELATE_OBSERVATION_KNOWLEDGE = RelationshipType.withName("Knowledge");

    public static final String START_NODE = "start";

    public static final String END_NODE = "end";

    public static final String RELATIONSHIP_TYPE = "relationship_type";
}