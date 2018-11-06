import org.neo4j.graphdb.*;
import org.neo4j.kernel.impl.api.store.RelationshipIterator;

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
        try (ResourceIterator<Node> graph1AllNodesIterator = MergeUtils.getAllNodesIteratorStatic(graph1); ResourceIterator<Node> graph2AllNodesIterator = MergeUtils.getAllNodesIteratorStatic(graph2)) {

            /* Put the nodes in the merged graph */
            while (graph1AllNodesIterator.hasNext() || graph2AllNodesIterator.hasNext()) {
                //assume the nodes do not exist unless shown otherwise
                Node nextNode1 = null;
                Node nextNode2 = null;
                //TODO: handle case when the graphs don't have same number of nodes
                try{
                    nextNode1 = graph1AllNodesIterator.next();
                    //Preemptively add the first node to graph 1.
                    if(nextNode1 != null){
                        String id1 = MergeUtils.getNodeID(graph1, nextNode1);
                        MergeUtils.putNodeInGraphStatic(mergedGraph, id1);
                    }
                } catch(Exception ignored){}

                try{
                    nextNode2 = graph2AllNodesIterator.next();
                } catch (Exception ignored){}

                //Only add the next node from graph 2 if it doesn't have the same id as the next node from graph 1.
                //So you won't add the same node twice.
                if(nextNode1 != null && nextNode2 != null){
                    //If the ids of the next nodes aren't equal, put in the next node from the second graph
                    if (!MergeUtils.getNodeID(graph1, nextNode1).equals(MergeUtils.getNodeID(graph2, nextNode2))) {
                        String id2 = MergeUtils.getNodeID(graph2, nextNode2);
                        MergeUtils.putNodeInGraphStatic(mergedGraph, id2);
                    }
                } else if (nextNode2 != null){
                    String id2 = MergeUtils.getNodeID(graph2, nextNode2);
                    MergeUtils.putNodeInGraphStatic(mergedGraph, id2);
                }
            }
        }
        /* Put the relationships in the merged graph */
        ArrayList<String> allMergedGraphIds = MergeUtils.getAllIDsStatic(mergedGraph);

        for(Object mergedGraphId : allMergedGraphIds){
            String currentMergedGraphId = mergedGraphId.toString();
            try {
                /*Get the appropriate node from either graph 1, graph 2, or both*/
                Node graph1Node = MergeUtils.getNodeByID(graph1, currentMergedGraphId);
                Node graph2Node = MergeUtils.getNodeByID(graph2, currentMergedGraphId);
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

//        //Take away duplicate relationships
//        ResourceIterator<Node> mergedGraphAllNodesIterator = MergeUtils.getAllNodesIteratorStatic(mergedGraph);
//        while(mergedGraphAllNodesIterator.hasNext()){
//            Node nextNode = mergedGraphAllNodesIterator.next();
//            String nextNodeId = MergeUtils.getNodeID(mergedGraph, nextNode);
//            Iterator<Relationship> rels = MergeUtils.getRelationshipIterator(mergedGraph, nextNode);
//            while(rels.hasNext()){
//                Relationship rel = rels.next();
//                Node[] relNodes = MergeUtils.getRelationshipNodes(mergedGraph, rel);
//                String recepientNodeId = MergeUtils.getNodeID(mergedGraph, relNodes[1]);
//                if(nextNodeId.equals(recepientNodeId)){ //If there is a relationship of a node to itself,
//                                             //or if the current node is the recipient of the relationship
//                    MergeUtils.deleteRelationship(mergedGraph, rel);
//                }
//            }
//        }
        return mergedGraph;
    }

    private static void connectSameRelationshipsInMergedGraph(GraphDatabaseService originalGraph, GraphDatabaseService mergedGraph, Node startNode){

        String startNodeId = MergeUtils.getNodeID(originalGraph, startNode);
        Relationship relationship;

        Iterator<Relationship> relationshipIterator = MergeUtils.getRelationshipIterator(originalGraph, startNode);
        while (relationshipIterator.hasNext()){

            relationship = relationshipIterator.next();
            RelationshipType relationshipType = MergeUtils.getRelationshipType(originalGraph, startNode, relationship);

            //Get both nodes in this relationship
            Node[] relationshipNodes = MergeUtils.getRelationshipNodes(originalGraph, relationship);

            //get id of the end node in the relationship (the second element in the array)
            String endNodeId = MergeUtils.getNodeID(originalGraph, relationshipNodes[1]);

            if(!startNodeId.equals(endNodeId)){ //To prevent relationships between a node and itself
                //get appropriate nodes in merged graph
                Node node1 = MergeUtils.getNodeByID(mergedGraph, startNodeId);
                Node node2 = MergeUtils.getNodeByID(mergedGraph, endNodeId);
                //connect the appropriate relationship
                if (node1 != null) {
                    MergeUtils.createRelationshipBetweenStatic(mergedGraph, node1, node2, relationshipType);
                }
            }
        }
    }
}