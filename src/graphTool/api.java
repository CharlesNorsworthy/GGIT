package graphTool;

/**
 * The GraphApi class will handle the outward facing api.
 * Possibly utilze Swagger
 */
public class GraphApi {
    public void createNode() {

    }

    public void readNode() {

    }

    public void updateNode() {

    }

    public void deleteNode() {

    }

    DbUtils database;


    DbGrapher(){
        this.database = new DbUtils();
        this.database.getConnection();
    }

    public void createRoot(int root)
    {
        database.createNode("Root","Root_id",root,1);
    }


    public void createObservation(String observationType)
    {

        database.createNode("Observation","Observation_type",observationType,1);

    }

    public void createKnowledge(String knowledgeType)
    {
        database.createNode("Knowledge","Knowledge_type",knowledgeType,1);
    }

    public void deleteAllObservationNodes()
    {
        database.deleteNodes("Observation");
    }

    public void deleteAllKnowledgeNodes()
    {
        database.deleteNodes("Knowledge");
    }

    public void deleteAllObservationNodesOfProperty(String prop, String propVal){
        database.deleteNodes("Observation", prop, propVal);
    }

    public void deleteAllKnowledgeNodesOfProperty(String prop, String propVal){
        database.deleteNodes("Knowledge", prop, propVal);
    }

    public void deleteAllRootsOfProperty(String prop, String propVal){
        database.deleteNodes("Root", prop, propVal);
    }
}
