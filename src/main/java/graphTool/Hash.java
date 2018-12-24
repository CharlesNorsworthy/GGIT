package graphTool;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.Hashtable;

/**
 * Create a new instance of this class for every branch (or graph?).
 * Place every node and relationship created into a hash
 * table.
 *
 */

public class Hash {

    Hashtable nodeHashTable;
    Hashtable relationshipHashTable;

    public void placeNodeInHashTable(DbUtils graph, Node node){

    }

    public void placeRelationshipInHashTable(DbUtils graph, Relationship relationship){

    }

    public void setNodeKey(){

    }

    public void setRelationshipKey(){

    }

    public int getNodeKey(){
        return 0;
    }

    public int getRelationshipKey(){
        return 0;
    }

    public Node getNodeByKey(){
        return null;
    }

    public Relationship getRelationshipByKey(){
        return null;
    }

}