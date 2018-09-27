package graphTool;

import org.neo4j.graphdb.Node;

/**
 * The GraphApi class will handle the outward facing api.
 * Possibly utilze Swagger
 */
public class GraphApi {

    graphTool.DbUtils database;

    public GraphApi(){
        this.database = new graphTool.DbUtils();
        this.database.getConnection();
    }

    public void createRoot(String root)
    {
        database.createNode("Root",root);

    }

    public void createObservation(String observationType)
    {
        database.createNode("Observation",observationType);

    }

    public void createKnowledge(String knowledgeType)
    {
        database.createNode("Knowledge",knowledgeType);
    }

    public void getObservationById(String id)
    {

         database.getNodeById("observation","id",id);
    }

    public void createRelationship(String node1,String node1_id,String node2, String node2_id)
    {
        database.createRelationship(node1,node1_id,node2,node2_id);

    }

    public void showObservationByType(String id)
    {
        database.printNode("Observation",id);
    }

    public void showRoot(String id)
    {
        database.printNode("Root",id);
    }

    public void deleteAllObservationNodes()
    {
        database.deleteNodes("Observation");

    }

    public void deleteAllKnowledgeNodes()
    {
        database.deleteNodes("Knowledge");

    }

    public void deleteAllObservationNodesOfProperty(String prop, String propVal)
    {
        database.deleteNodes("Observation", prop, propVal);

    }

    public void deleteAllKnowledgeNodesOfProperty(String prop, String propVal)
    {
        database.deleteNodes("Knowledge", prop, propVal);

    }

    public void deleteAllRootsOfProperty(String prop, String propVal)
    {
        database.deleteNodes("Root", prop, propVal);

    }
}
