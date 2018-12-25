package graphTool;

import org.neo4j.graphdb.Relationship;

import java.util.Hashtable;

/**
 * Create a new instance of this class for every branch (or graph?).
 * Place every node and relationship created into a hash
 * table.
 *
 */
public class RelationshipHash {

    private Hashtable<Long, Relationship> relationshipHashTable;

    public void placeRelationshipInHashTable(Long uuid, Relationship relationship){
        relationshipHashTable.put(uuid, relationship);
    }

    public void setRelationshipKey(){

    }

    public int getRelationshipKey(){
        return 0;
    }

    public Relationship getRelationshipByKey(){
        return null;
    }

}