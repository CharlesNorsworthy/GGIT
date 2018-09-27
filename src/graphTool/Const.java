package graphTool;

import java.util.Arrays;
import java.util.List;

/**
 * Const holds variables used throughout GGIT/graphTool
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
    public static final String ROOT_LABEL = "ROOT";

    public static final String OBSERVATION_LABEL = "OBSERVATION";

    public static final String KNOWLEDGE_LABEL = "KNOWLEDGE";

    /**
     * Database Relationships
     */
    public static final String RELATE_ROOT_OBS = "Observation";

    public static final String RELATE_OBS_KNOW = "Knowledge";
}