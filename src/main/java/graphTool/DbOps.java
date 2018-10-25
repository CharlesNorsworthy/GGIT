package graphTool;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import scala.collection.immutable.Stream;

import java.util.HashMap;
import java.util.List;

public class DbOps {
    DbUtils db;

    Node root;

    /** DbOps Constructor
     *  The user must enter in a path for the database to be initialized.
     * @param dbPath
     */
    public DbOps(String dbPath) {
        db = new DbUtils(dbPath);
        root = db.initRoot();
        //db.createDefaultNodes();
        //db.deleteNodesByType(Const.ROOT_LABEL);
        //db.deleteNodesByType(Const.OBSERVATION_LABEL);
        //db.deleteNodesByType(Const.KNOWLEDGE_LABEL);
    }

    public void createObservation(HashMap<String, Object> props) {
        Node obsNode = db.createNode(Const.OBSERVATION_LABEL, props);
        db.createRelationship(Const.ROOT_LABEL, root, Const.OBSERVATION_LABEL, obsNode);
    }

    public HashMap<String, Object> readObservation(String id) {
        Node node = (db.getNodeById(Const.OBSERVATION_LABEL, id));
        return db.readNodeMap(node);
    }

    public void updateObservation(String id, HashMap<String, Object> props) {
        Node node = db.getNodeById(Const.OBSERVATION_LABEL, id);
        db.updateNode(node, props);
    }

    public void deleteObservation(String id) {
        db.deleteNode(Const.OBSERVATION_LABEL, id);
    }

    public void createKnowledge(String obsId, HashMap<String, Object> props) {
        Node knowNode = db.createNode(Const.KNOWLEDGE_LABEL, props);
        Node obsNode = db.getNodeById(Const.OBSERVATION_LABEL, obsId);
        db.createRelationship(Const.OBSERVATION_LABEL, obsNode, Const.KNOWLEDGE_LABEL, knowNode);
    }

    public HashMap<String, Object> readKnowledge(String id) {
        Node node = (db.getNodeById(Const.KNOWLEDGE_LABEL, id));
        return db.readNodeMap(node);
    }

    public HashMap<String, HashMap<String, Object>> readAllKnowledges() {
        List<Node> nodeList = db.getNodesByType(Const.KNOWLEDGE_LABEL);
        return db.readNodesMap(nodeList);
    }

    public HashMap<String, HashMap<String, Object>> readAllObservations() {
        List<Node> nodeList = db.getNodesByType(Const.OBSERVATION_LABEL);
        return db.readNodesMap(nodeList);
    }

    public void updateKnowledge(String id, HashMap<String, Object> props) {
        Node node = db.getNodeById(Const.KNOWLEDGE_LABEL, id);
        db.updateNode(node, props);
    }

    public void deleteKnowledge(String id) {
        db.deleteNode(Const.KNOWLEDGE_LABEL, id);
    }

    public List<Relationship> readAllRelationShips(){
        return db.getRelationships();
    }

    public void createDefaultNodes(){
        db.createDefaultNodes();
    }

    public void deleteeAllNodes(){
        db.deleteNodesByType(Const.OBSERVATION_LABEL);
        db.deleteNodesByType(Const.KNOWLEDGE_LABEL);
        db.deleteNodesByType(Const.ROOT_LABEL);
    }

    public void close(){
        db.dispose();
    }
}