package graphTool;

<<<<<<< HEAD
import org.neo4j.graphdb.Node;

=======
>>>>>>> 12f1d15a1b2be46d153f0793ee21072665952df6
/**
 * The GraphApi class will handle the outward facing api.
 * Possibly utilze Swagger
 */
public class GraphApi {

<<<<<<< HEAD
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
=======
    public void createObservation() {

    }

    public void readObservation() {

    }

    public void updateObservation() {

    }

    public void deleteObservation() {

    }

    public void createKnowledge() {

    }

    public void readKnowledge() {

    }

    public void updateKnowledge() {

    }

    public void deleteKnowledge() {
>>>>>>> 12f1d15a1b2be46d153f0793ee21072665952df6

    }
}
