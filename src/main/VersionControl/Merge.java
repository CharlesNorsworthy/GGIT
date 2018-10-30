import org.neo4j.graphdb.*;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * Created by cNorsworthy 9/27/18
 *
 * This class contains methods and helper methods for merging
 * two graphs together.
 *
 */

class Merge {

    /**
     *
     * This is used in merging within a branch when someone pulls from master.
     *
     * We are merging two graph databases by adding their nodes and
     * relationships into another graph database. This naive merge
     * assumes no conflicts. Edges are NOT weighted.
     *
     * @param graph1
     * @param graph2
     * @return GraphDatabaseService mergedGraph
     */
//TODO: refactor
    static GraphDatabaseService mergeMe(GraphDatabaseService graph1, GraphDatabaseService graph2, GraphDatabaseService mergedGraph){

        //Search through graph1 and graph2 (breadth first search) and get all nodes and what each node is connected to
        try (ResourceIterator<Node> graph1AllNodesIterator = DbUtils.getAllNodesIteratorStatic(graph1); ResourceIterator<Node> graph2AllNodesIterator = DbUtils.getAllNodesIteratorStatic(graph2)) {

            //Put the nodes in the merged graph
            while (graph1AllNodesIterator.hasNext() || graph2AllNodesIterator.hasNext()) {
                //assume the nodes do not exist unless shown otherwise
                boolean nextNode1Exists = false;
                boolean nextNode2Exists = false;
                //TODO: handle case when the graphs don't have same number of nodes
                //Preemptively add the first node to graph 1.
                Node nextNode1 = graph1AllNodesIterator.next();
                if(nextNode1 != null){
                    String id1 = DbUtils.getNodeID(graph1, nextNode1);
                    DbUtils.putNodeInGraphStatic(mergedGraph, id1);
                    nextNode1Exists = true;
                }

                Node nextNode2 = graph2AllNodesIterator.next();
                if(nextNode2 != null){
                    nextNode2Exists = true;
                }

                //Only add the next node from graph 2 if it doesn't have the same id as the next node from graph 1.
                //So you won't add the same node twice.
                if(nextNode1Exists && nextNode2Exists){
                    //If the ids of the next nodes aren't equal, put in the next node from the second graph
                    if (!DbUtils.getNodeID(graph1, nextNode1).equals(DbUtils.getNodeID(graph2, nextNode2))) {
                        String id2 = DbUtils.getNodeID(graph2, nextNode2);
                        DbUtils.putNodeInGraphStatic(mergedGraph, id2);
                    }
                } else if (nextNode2Exists){
                    String id2 = DbUtils.getNodeID(graph2, nextNode2);
                    DbUtils.putNodeInGraphStatic(mergedGraph, id2);
                }
            }
        }
        //Put the relationships in the merged graph
        ArrayList<String> allMergedGraphIds = DbUtils.getAllIDsStatic(mergedGraph);

        for(Object mergedGraphId : allMergedGraphIds){
            String currentMergedGraphId = mergedGraphId.toString();
            try {
                /*Get the appropriate node from either graph 1, graph 2, or both*/
                Node graph1Node = DbUtils.getNodeByID(graph1, currentMergedGraphId);
                Node graph2Node = DbUtils.getNodeByID(graph2, currentMergedGraphId);
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

            //TODO: maybe edit relationshipNodes to exclude the current node
            Node[] relationshipNodes = DbUtils.getRelationshipNodes(originalGraph, relationship);
            for(Node relationshipNode : relationshipNodes){
                //get id's of the nodes
                String id1 = DbUtils.getNodeID(originalGraph, node);
                String id2 = DbUtils.getNodeID(originalGraph, relationshipNode);
                //get appropriate nodes in merged graph
                Node node1 = DbUtils.getNodeByID(mergedGraph, id1);
                Node node2 = DbUtils.getNodeByID(mergedGraph, id2);
                //connect the appropriate relationship
                if (node1 != null) {
                    DbUtils.createRelationshipBetweenStatic(mergedGraph, node1, node2, relationshipType);
                }
            }
        }
    }
}