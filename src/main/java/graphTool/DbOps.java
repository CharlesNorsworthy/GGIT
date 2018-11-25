package graphTool;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;
import java.util.List;

public class DbOps implements DatabaseBuilder{
    DbUtils db;

    Node root;

    /** DbOps Constructor
     *  The user must enter in a path for the database to be initialized.
     * @param dbPath
     */
    public DbOps(String dbPath) {
        db = new DbUtils(dbPath);
        root = db.initRoot();
    }

    /** DbOps Constructor
     *  The user must enter in a path for the database to be initialized.
     *  The user also enters in the uuid for the root
     * @param dbPath
     * @param uuid
     */
    public DbOps(String dbPath, String uuid) {
        db = new DbUtils(dbPath);
        root = db.initRoot(uuid);
    }

    @Override
    public Label getLabel(Relationship rel) {
        if (rel == Const.RELATE_ROOT_OBSERVATION) {
            return Const.OBSERVATION_LABEL;
        } else if (rel == Const.RELATE_OBSERVATION_KNOWLEDGE){
            return Const.KNOWLEDGE_LABEL;
        }
        throw new IllegalArgumentException("This relationship '" + rel + "' should not exist in the database!");
    }

    public void createObservation(HashMap<String, Object> props) {
        Node obsNode = db.createNode(Const.OBSERVATION_LABEL, props);
        db.createRelationship(root, obsNode, Const.RELATE_ROOT_OBSERVATION);
    }

    public HashMap<String, Object> getObservation(String id) {
        Node node = db.readNode(Const.OBSERVATION_LABEL, id);
        return db.readNodeProperties(node);
    }

    public HashMap<String, HashMap<String, Object>> getAllObservations() {
        List<Node> nodes = db.readNodes(Const.OBSERVATION_LABEL);
        return db.readNodeListProperties(nodes);
    }

    public void updateObservation(String id, HashMap<String, Object> props) {
        Node node = db.readNode(Const.OBSERVATION_LABEL, id);
        db.updateNode(node, props);
    }

    public void deleteObservation(String id) {
        db.deleteNode(Const.OBSERVATION_LABEL, id, this);
    }

    public void createKnowledge(String obsId, HashMap<String, Object> props) {
        Node knowNode = db.createNode(Const.KNOWLEDGE_LABEL, props);
        Node obsNode = db.readNode(Const.OBSERVATION_LABEL, obsId);
        db.createRelationship(obsNode, knowNode, Const.RELATE_OBSERVATION_KNOWLEDGE);
    }

    public HashMap<String, Object> getKnowledge(String id) {
        Node node = db.readNode(Const.KNOWLEDGE_LABEL, id);
        return db.readNodeProperties(node);
    }

    public HashMap<String, HashMap<String, Object>> getAllKnowledges() {
        List<Node> nodes = db.readNodes(Const.KNOWLEDGE_LABEL);
        return db.readNodeListProperties(nodes);
    }

    public void updateKnowledge(String id, HashMap<String, Object> props) {
        Node node = db.readNode(Const.KNOWLEDGE_LABEL, id);
        db.updateNode(node,  props);
    }

    public void deleteKnowledge(String id) {
        db.deleteNode(Const.KNOWLEDGE_LABEL, id, this);
    }

    public HashMap<String, Object> getObservationRelationship(String obsId){
        Node obsNode = db.readNode(Const.OBSERVATION_LABEL, obsId);
        Relationship rel = db.readRelationship(db.initRoot(), obsNode, Const.RELATE_ROOT_OBSERVATION);
        return db.readRelationshipProperties(rel);
    }

    public HashMap<String, Object> getKnowledgeRelationship(String obsId , String knowId){
        Node obsNode = db.readNode(Const.OBSERVATION_LABEL, obsId);
        Node knowNode = db.readNode(Const.KNOWLEDGE_LABEL, knowId);
        Relationship rel = db.readRelationship(obsNode, knowNode, Const.RELATE_OBSERVATION_KNOWLEDGE);
        return db.readRelationshipProperties(rel);
    }

    public HashMap<String, HashMap<String, Object>> getObservationRelationships(String id){
        Node node = db.readNode(Const.OBSERVATION_LABEL, id);
        List<Relationship> rels = db.readRelationships(node);
        return db.readRelationshipListProperties(rels);
    }

    public HashMap<String, HashMap<String, Object>> getKnowledgeRelationships(String id){
        Node node = db.readNode(Const.KNOWLEDGE_LABEL, id);
        List<Relationship> rels = db.readRelationships(node);
        return db.readRelationshipListProperties(rels);
    }

    public HashMap<String, HashMap<String, Object>> getAllRelationShips(){
        List<Relationship> rels =  db.readAllRelationships();
        return db.readRelationshipListProperties(rels);
    }

    public HashMap<String, HashMap<String, Object>> getAllObservationRelationShips(){
        List<Relationship> rels =  db.readAllRelationships(Const.RELATE_ROOT_OBSERVATION);
        return db.readRelationshipListProperties(rels);
    }

    public HashMap<String, HashMap<String, Object>> getAllKnowledgeRelationShips(){
        List<Relationship> rels =  db.readAllRelationships(Const.RELATE_OBSERVATION_KNOWLEDGE);
        return db.readRelationshipListProperties(rels);
    }

    public void deleteObservationRelationship(String obsId){
        Node obsNode = db.readNode(Const.OBSERVATION_LABEL, obsId);
        Relationship rel = db.readRelationship(db.initRoot(), obsNode, Const.RELATE_ROOT_OBSERVATION);
        db.deleteRelationship(rel);
    }

    public void deleteKnowledgeRelationship(String obsId, String knowId){
        Node obsNode = db.readNode(Const.OBSERVATION_LABEL, obsId);
        Node knowNode = db.readNode(Const.KNOWLEDGE_LABEL, knowId);
        Relationship rel = db.readRelationship(obsNode, knowNode, Const.RELATE_ROOT_OBSERVATION);
        db.deleteRelationship(rel);
    }

    public void createDefaultNodes(){
        db.createDefaultNodes();
    }

    public void deleteAllNodes(){
        db.deleteNodes(Const.OBSERVATION_LABEL);
        db.deleteNodes(Const.KNOWLEDGE_LABEL);
        db.deleteNodes(Const.ROOT_LABEL);
    }

    public void close(){
        db.dispose();
    }
}