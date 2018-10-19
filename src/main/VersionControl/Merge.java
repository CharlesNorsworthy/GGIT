
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
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
        try (ResourceIterator<Node> graph1AllNodesIterator = DbUtils.getAllNodesIterator(graph1); ResourceIterator<Node> graph2AllNodesIterator = DbUtils.getAllNodesIterator(graph2)) {
            //Put the nodes in the merged graph
            while (graph1AllNodesIterator.hasNext() || graph2AllNodesIterator.hasNext()) {
                //Preemptively add the first node to graph 1.
                Node nextNode1 = graph1AllNodesIterator.next();
                String id = DbUtils.getNodeID(graph1, nextNode1);
                DbUtils.putNodeInGraph(mergedGraph, id);
                //connectRelationships(graph1, mergedGraph, nextNode1);

                //Only add the next node from graph 2 if it doesn't have the same id as the next node from graph 1.
                //So you won't add the same node twice.
                Node nextNode2 = graph2AllNodesIterator.next();

                //If the ids of the next nodes aren't equal, put in the next node from the second graph
                if (!DbUtils.getNodeID(graph1, nextNode1).equals(DbUtils.getNodeID(graph2, nextNode2))) {
                    String id2 = DbUtils.getNodeID(graph2, nextNode2);
                    DbUtils.putNodeInGraph(mergedGraph, id2);
                    //connectRelationships(graph2, mergedGraph, nextNode2);
                }
            }
        }
        //Put the relationships in the merged graph
        try (ResourceIterator<Node> graph1AllNodesIterator = DbUtils.getAllNodesIterator(graph1); ResourceIterator<Node> graph2AllNodesIterator = DbUtils.getAllNodesIterator(graph2)) {
            while (graph1AllNodesIterator.hasNext() || graph2AllNodesIterator.hasNext()) {
                Node nextNode1 = graph1AllNodesIterator.next();

                Node nextNode2 = graph2AllNodesIterator.next();

            }
        }
        return mergedGraph;
    }

    private static void putNodeInGraph(GraphDatabaseService graph, Node node){
        String id = DbUtils.getNodeID(graph, node);
        DbUtils.putNodeInGraph(graph, id);
    }

    private static void connectRelationships(GraphDatabaseService originalGraph, GraphDatabaseService mergedGraph, Node node){
        Iterator<Relationship> relationshipIterator = DbUtils.getRelationshipIterator(originalGraph, node);
        Relationship relationship;
        while (relationshipIterator.hasNext()){

            relationship = relationshipIterator.next();
            RelationshipType relationshipType = DbUtils.getRelationshipType(originalGraph, node, relationship);

            Node[] relationshipNodes = DbUtils.getRelationshipNodes(originalGraph, relationship);
            for(Node relationshipNode : relationshipNodes){
                DbUtils.createRelationshipBetween(mergedGraph, node, relationshipNode, relationshipType);
            }
        }
    }
}