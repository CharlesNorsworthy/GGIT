import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

class Merge {

    /**
     *
     * Created by cNorsworthy 9/27/18
     *
     * This class contains methods and helper methods for merging
     * two graphs together.
     *
     */

    //This is used in merging within a branch when someone pulls from master
    static GraphDatabaseService mergeMe(GraphDatabaseService graph1, GraphDatabaseService graph2){

        //We are actually merging two graph databases by adding their nodes into another graph database
        //This naive merge only works assuming no conflicts
        //Edges are NOT weighted

        GraphDatabaseService mergedGraph = new GraphDatabaseFactory().newEmbeddedDatabase(new File("\\C:\\MergedGraph"));

        //Search through graph1 and graph2 (breadth first search) and get all nodes and what each node is connected to
        try (ResourceIterator<Node> graph1AllNodesIterator = DbUtils.getAllNodesIteratorStatic(graph1); ResourceIterator<Node> graph2AllNodesIterator = DbUtils.getAllNodesIteratorStatic(graph2)) {
            //Put the nodes in the merged graph
            while (graph1AllNodesIterator.hasNext() || graph2AllNodesIterator.hasNext()) {
                //Preemptively add the first node to graph 1.
                Node nextNode1 = graph1AllNodesIterator.next();
                String id = DbUtils.getNodeID(graph1, nextNode1);
                DbUtils.putNodeInGraphStatic(mergedGraph, id);

                //Only add the next node from graph 2 if it doesn't have the same id as the next node from graph 1.
                //So you won't add the same node twice.
                Node nextNode2 = graph2AllNodesIterator.next();

                //If the ids of the next nodes aren't equal, put in the next node from the second graph
                if (!DbUtils.getNodeID(graph1, nextNode1).equals(DbUtils.getNodeID(graph2, nextNode2))) {
                    String id2 = DbUtils.getNodeID(graph2, nextNode2);
                    DbUtils.putNodeInGraphStatic(mergedGraph, id2);
                }
            }
        }
        //TODO: put in relationships
        //Put the relationships in the merged graph
        ArrayList<String> allIds = DbUtils.getAllIDsStatic(mergedGraph);

        for(Object allId : allIds){
            String currentId = allId.toString();
            try {
                /*Get the appropriate node from either graph 1, graph 2, or both*/
                Node graph1Node = DbUtils.getNodeByProperty(graph1, "ID", currentId);
                Node graph2Node = DbUtils.getNodeByProperty(graph2, "ID", currentId);
                if ((graph1Node != null) && (graph2Node != null)) {
                    //put in relationships for both
                    connectSameRelationshipsInMergedGraph(graph1, mergedGraph, graph1Node);
                    connectSameRelationshipsInMergedGraph(graph2, mergedGraph, graph2Node);
                    //connectAllRelationships
                } else if ((graph1Node == null) && (graph2Node != null)) {
                    //put in relationship for node2 only
                    connectSameRelationshipsInMergedGraph(graph2, mergedGraph, graph2Node);
                } else if (graph1Node != null) {
                    //put in relationship for node1 only
                    connectSameRelationshipsInMergedGraph(graph1, mergedGraph, graph1Node);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mergedGraph;
    }

    private static void connectSameRelationshipsInMergedGraph(GraphDatabaseService originalGraph, GraphDatabaseService mergedGraph, Node node){
        Iterator<Relationship> relationshipIterator = DbUtils.getRelationshipIterator(originalGraph, node);
        Relationship relationship;
        while (relationshipIterator.hasNext()){

            relationship = relationshipIterator.next();
            RelationshipType relationshipType = DbUtils.getRelationshipType(originalGraph, node, relationship);

            Node[] relationshipNodes = DbUtils.getRelationshipNodes(originalGraph, relationship);
            for(Node relationshipNode : relationshipNodes){
                //get id's of the nodes
                String id1 = DbUtils.getNodeID(originalGraph, node);
                String id2 = DbUtils.getNodeID(originalGraph, relationshipNode);
                //get appropriate nodes in merged graph
                Node node1 = DbUtils.getNodeByProperty(mergedGraph, "ID", id1);
                Node node2 = DbUtils.getNodeByProperty(mergedGraph, "ID", id2);
                //connect the appropriate relationship
                if (node1 != null) {
                    DbUtils.createRelationshipBetweenStatic(mergedGraph, node1, node2, relationshipType);
                }
            }
        }
    }



//    public static GraphDatabaseService connectRelationshipsInNewGraph(){
//
//    }
}