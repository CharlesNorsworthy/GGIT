package VersionControl;

import graphTool.DbOps;
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

public class Merge {

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
     * @return DbOps mergedGraph
     */
    //TODO: refactor
    //TODO: use DbOps graphs
    public static DbOps mergeMe(DbOps graph1, DbOps graph2, DbOps mergedGraph){

        //Search through graph1 and graph2 (breadth first search) and get all nodes and what each node is connected to
        try (ResourceIterator<Node> graph1AllNodesIterator = graph1.getAllNodesIterator(); ResourceIterator<Node> graph2AllNodesIterator = graph2.getAllNodesIterator()) {

            /* Put the nodes in the merged graph */
            while (graph1AllNodesIterator.hasNext() || graph2AllNodesIterator.hasNext()) {
                //assume the nodes do not exist unless shown otherwise
                Node nextNode1 = null;
                Node nextNode2 = null;
                try{
                    nextNode1 = graph1AllNodesIterator.next();
                    //Preemptively add the first node to graph 1.
                    if(nextNode1 != null){
                        String id1 = graph1.getNodeID(nextNode1);
                        mergedGraph.putNodeInGraph(id1);
                    }
                } catch(Exception ignored){}

                try{
                    nextNode2 = graph2AllNodesIterator.next();
                } catch (Exception ignored){}

                //Only add the next node from graph 2 if it doesn't have the same id as the next node from graph 1.
                //So you won't add the same node twice.
                if(nextNode1 != null && nextNode2 != null){
                    //If the ids of the next nodes aren't equal, put in the next node from the second graph
                    if (!graph1.getNodeID(nextNode1).equals(graph2.getNodeID(nextNode2))) {
                        String id2 = graph2.getNodeID(nextNode2);
                        mergedGraph.putNodeInGraph(id2);
                    }
                } else if (nextNode2 != null){
                    String id2 = graph2.getNodeID(nextNode2);
                    mergedGraph.putNodeInGraph(id2);
                }
            }
        }
        /* Put the relationships in the merged graph */
        ArrayList<String> allMergedGraphIds = mergedGraph.getAllIDs();

        for(Object mergedGraphId : allMergedGraphIds){
            String currentMergedGraphId = mergedGraphId.toString();
            try {
                /*Get the appropriate node from either graph 1, graph 2, or both*/
                Node graph1Node = graph1.getNodeByID(currentMergedGraphId);
                Node graph2Node = graph2.getNodeByID(currentMergedGraphId);
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

    private static void connectSameRelationshipsInMergedGraph(DbOps originalGraph, DbOps mergedGraph, Node startNode){

        String startNodeId = originalGraph.getNodeID(startNode);
        Relationship relationship;

        Iterator<Relationship> relationshipIterator = originalGraph.getRelationshipIterator(startNode);
        while (relationshipIterator.hasNext()){

            relationship = relationshipIterator.next();
            RelationshipType relationshipType = originalGraph.getRelationshipType(relationship);

            //Get both nodes in this relationship
            Node[] relationshipNodes = originalGraph.getRelationshipNodes(relationship);

            //(the second element in the array)
            Node endNode = relationshipNodes[1];
            //get id of the end node in the relationship
            String endNodeId = originalGraph.getNodeID(endNode);

            if(!startNodeId.equals(endNodeId)){ //To prevent relationships between a node and itself, an to prevent adding the same rel twice
                //get appropriate nodes in merged graph
                Node node1 = mergedGraph.getNodeByID(startNodeId);
                Node node2 = mergedGraph.getNodeByID(endNodeId);

                if (node1 != null) {
                    Relationship preExistingRelationship = mergedGraph.getRelationshipBetween(node1, endNodeId);
                    if(preExistingRelationship == null){ //to make sure you aren't adding the same relationship twice
                        //connect the appropriate relationship
                        mergedGraph.createRelationshipBetween(node1, node2, relationshipType);
                    }
                }
            }
        }
    }
}