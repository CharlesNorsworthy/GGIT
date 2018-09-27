package graphTool;

import org.neo4j.graphdb.Node;

import java.util.HashMap;
import java.util.List;

public class DbOps {
    DbUtils db = new DbUtils();
    Node root = db.init();

    DbOps() {
        db = new DbUtils();
        db.getConnection();
    }

    public void createObservation(HashMap<String, Object> props) {
        Node obsNode = db.createNode(Const.OBSERVATION_LABEL, props);
        db.createRelationship(Const.ROOT_LABEL, root, Const.OBSERVATION_LABEL, obsNode);
    }

    public HashMap<String, Object> readObservation(String id) {
        return (db.getNodeById(id)).getAllProperties();
    }

    public HashMap<String, HashMap<String, Object>> readAllObservations() {
        HashMap<String, HashMap<String, Object>> nodes = new HashMap<>();
        List<Node> nodeList = db.getNodesByType(Const.OBSERVATION_LABEL);
        nodeList.iterator().forEachRemaining( node -> {
            nodes.put((String) node.getProperty(Const.UUID), (HashMap) node.getAllProperties());
        });
        return nodes;
    }

    public void updateObservation(String id, HashMap<String, Object> props) {
        db.updateNode(id, props);
    }

    public void deleteObservation(String id) {
        db.deleteNode(id);
    }

    public void createKnowledge(String obsId, HashMap<String, Object> props) {
        Node knowNode = db.createNode(Const.KNOWLEDGE_LABEL, props);
        Node obsNode = db.getNodeById(obsId);
        db.createRelationship(obsNode, knowNode);
    }

    public HashMap<String, Object> readKnowledge(String id) {
        HashMap<String, Object> props = (db.getNodeById(id)).getAllProperties();
        return props;
    }

    public HashMap<String, HashMap<String, Object>> readAllKnowledge() {
        HashMap<String, HashMap<String, Object>> nodes = new HashMap<>();
        List<Node> nodeList = db.getNodesByType(Const.KNOWLEDGE_LABEL);
        nodeList.iterator().forEachRemaining( node -> {
            nodes.put((String) node.getProperty(Const.UUID), (HashMap) node.getAllProperties());
        });
        return nodes;
    }

    public void updateKnowledge(String id, HashMap<String, Object> props) {
        db.updateNode(id, props);
    }

    public void deleteKnowledge(String id) {
        db.deleteNode(id);
    }
}