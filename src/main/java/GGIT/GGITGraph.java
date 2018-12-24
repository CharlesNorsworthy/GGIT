package GGIT;

import graphTool.Const;
import graphTool.DbUtils;
import org.neo4j.driver.v1.exceptions.DatabaseException;
import org.neo4j.graphdb.*;
import scala.collection.immutable.Stream;

import java.util.*;

import static org.neo4j.graphdb.Direction.INCOMING;
import static org.neo4j.graphdb.Direction.OUTGOING;

@SuppressWarnings("unchecked")
public class GGITGraph{
    DbUtils db;

    Node root;

    public GGITGraph(String dbPath) {
        this.db = new DbUtils(dbPath);
    }

    /**
     * Used to initialize the root of the repository
     * @param graphRef
     */
    public String initRepo(String graphRef) {
        String uuid = UUID.randomUUID().toString();
        HashMap<String, Object> props = new HashMap<>();
        props.put(Const.UUID, uuid);
        props.put(GGITConst.GRAPH_REFERENCE, graphRef);
        props.put(GGITConst.BRANCH, GGITConst.MASTER);
        props.put(GGITConst.TIMESTAMP, (new Date()).getTime());
        this.root = this.db.createNode(Label.label(GGITConst.MASTER), props);
        return uuid;
    }

    /**
     * Used to create and add a node to the graph. Creates a relationship the previous "currentNode". Returns the new node's UUID.
     * @param graphRef
     * @param branch
     * @param message
     * @param prevNode
     * @return
     */
    public String addNode(String graphRef, String branch, String message, String prevNode) {
        HashMap<String, Object> props = new HashMap<>();
        String uuid = UUID.randomUUID().toString();
        props.put(Const.UUID, uuid);
        props.put(GGITConst.GRAPH_REFERENCE, graphRef);
        props.put(GGITConst.BRANCH, branch);
        props.put(GGITConst.MESSAGE, message);
        props.put(GGITConst.TIMESTAMP, (new Date()).getTime());
        Node current = db.createNode(Label.label(branch), props);

        if (prevNode != null || !prevNode.isEmpty() || !prevNode.equals(uuid)){
            Node previous = db.readNode(Label.label(branch), prevNode);
            db.createRelationship(previous, current, RelationshipType.withName(branch));
        }

        return uuid;
    }

    public void updateNodeGraphRef(String branch, String uuid, String graphRefPath){
        HashMap<String, Object> props = new HashMap<>();
        props.put(GGITConst.GRAPH_REFERENCE, graphRefPath);
        Node node = db.readNode(Label.label(branch), uuid);
        db.updateNode(node, props);
    }

    /**
     * Removes specified node from branch
     * @param label
     * @param uuid
     */
    public void removeNode(Label label, String uuid){
        db.deleteNode(label, uuid);
    }

    /**
     * Used to get the properties of a node
     * @param uuid
     */
    public HashMap<String, Object> readNode(String uuid, String branch) {
        Node node = this.db.readNode(Label.label(branch), uuid);
        return this.db.readNodeProperties(node);
    }

    /**
     * Retrieves all labels from the database
     * @return
     */
    public List<Label> getLabels() {
        if (this.db != null) {
            return this.db.getLabels();
        } else {
            throw new DatabaseException("400", "Database not initialized");
        }
    }

    /**
     * Used to check if the listed "current" node is actually the most current
     * @param uuid
     * @param branch
     */
    public long getTimeStamp(String uuid, String branch) {
        return (long) this.db.readNode(Label.label(branch), uuid).getProperty(GGITConst.TIMESTAMP);
    }

    /**
     * Gets the "current" node on a given branch
     * @param label
     * @return
     */
    public String getCurrNode(String label) {
        List<Node> nodes = this.db.readNodes(Label.label(label));
        if (nodes.isEmpty()) {
            throw new IllegalArgumentException("There are no nodes in the repository on branch '" + label + "'");
        }
        for (Node node : nodes) {
            if(!this.db.checkOutgoingRelationship(node)) {
                return this.db.getNodeID(node);
            }
        }
        throw new IllegalArgumentException("This branch has been merged into another branch");
    }

    /**
     * Returns true if specified branch exists in the repository.
     * @param branch
     * @return
     */
    public boolean doesBranchExist(String branch) {
        boolean branchExists = false;
        if (this.db != null) {
            List<Label> labels = this.db.getLabels();
            for (Label label : labels) {
                if (label.name().equals(branch)) {
                    branchExists = true;
                }
            }
        } else {
            throw new DatabaseException("400", "Database not initialized");
        }
        return branchExists;
    }

    /**
     * Returns true if specified node is the first node in a specified branch. Will return true for root.
     * @param branch
     * @param uuid
     * @return
     */
    public boolean isStartOfBranch(Label branch, String uuid){
        Node node = db.readNode(branch, uuid);
        if (node == null) {
            throw new IllegalArgumentException("There are no nodes in the repository on branch '" + branch + "'");
        }
        else if(!this.db.checkIncomingRelationship(node)) {
            return true;
        }
        else {
            String startNode = null;
            List<Relationship> rels = db.readRelationships(node);
            for(Relationship rel : rels){
                if(db.readRelationshipProperties(rel).get(Const.END_NODE).toString().equals(uuid)){
                    startNode = db.readRelationshipProperties(rel).get(Const.START_NODE).toString();
                    break;
                }
            }

            if(startNode != null){
                List<Label> labels = db.getLabels();
                labels.removeIf( label -> (label == branch));
                for(Label label : labels){
                    if(db.readNode(label,startNode) != null ){
                        return true;
                    }
                }
                
                return false;
            } else {
                return false;
            }
        }
    }


    /**
     * Closes the instance of the graph
     */
    public void closeGraph(){
        this.db.dispose();
    }
}
