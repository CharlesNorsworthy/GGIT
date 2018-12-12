package VersionControl;

import graphTool.Const;
import graphTool.DbUtils;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

import java.util.ArrayList;
import java.util.HashMap;

//TODO: eventually rename to just Merge after its done
public class OptimizedMerge {

    /**
     * This merge computes the union of two graphs by computing the union of their edge
     * and vertices sets. This can be accomplished in O(max(|e1|*k, |e2|*k) + max(|v1|, |v2|))
     * time, where e1, e2 and v1, v2 are the edge (node) and vertex (relationship) sets
     * of graph1 and graph2 respectively. The cardinality, or number of elements in a set s,
     * is denoted |s|. The number k is the number of properties in a node.
     *
     * @param graph1 the
     * @param graph2
     * @param mergedGraph
     * @return DbUtils
     */

    public static DbUtils naiveMerge(DbUtils graph1, DbUtils graph2, DbUtils mergedGraph){

        mergedGraph = computeUnionOfNodes(graph1, graph2, mergedGraph);

        mergedGraph = computeUnionOfRelationships(graph1, graph2, mergedGraph);

        return mergedGraph;
    }

    /**
     * Computes the union of the nodes in the two graphs and puts the
     * result in the merged graph.
     *
     * @param graph1 the original graph
     * @param graph2 the other graph?
     * @param mergedGraph the resulting graph
     * @return DbUtils
     */

    private static DbUtils computeUnionOfNodes(DbUtils graph1, DbUtils graph2, DbUtils mergedGraph){
        //TODO: optimize and refactor
        //TODO: reduce amount of try/catch statements

        //Get all nodes from both graphs
        try (ResourceIterator<Node> graph1AllNodesIterator = graph1.getAllNodesIterator();
             ResourceIterator<Node> graph2AllNodesIterator = graph2.getAllNodesIterator()){

            /* Put the nodes in the merged graph */
            while (graph1AllNodesIterator.hasNext() || graph2AllNodesIterator.hasNext()){
                //Assume the nodes do not exist unless shown otherwise
                Node nextNode1 = null;
                Node nextNode2 = null;

                try {
                    nextNode1 = graph1AllNodesIterator.next();
                } catch (Exception ignored){}

                try {
                    nextNode2 = graph2AllNodesIterator.next();
                } catch (Exception ignored){}

                /* Checks every possible case the nodes can exist in */
                if (nextNode1 != null && nextNode2 != null){
                    //They both exist, add them both.
                    includeNodeInMergedGraph(mergedGraph, graph1, nextNode1);
                    //Only add the second one if it doesn't have the same ID as the first
                    if (!graph1.getNodeID(nextNode1).equals(graph2.getNodeID(nextNode2))){
                        includeNodeInMergedGraph(mergedGraph, graph2, nextNode2);
                    }
                } else if(nextNode1 != null){
                    //They don't both exist, does node 1 exist?
                    includeNodeInMergedGraph(mergedGraph, graph1, nextNode1);
                } else if (nextNode2 != null){
                    //They don't both exist, does node 2 exist?
                    includeNodeInMergedGraph(mergedGraph, graph2, nextNode2);
                }
            }
        }
        return mergedGraph;
    }

    private static void includeNodeInMergedGraph(DbUtils mergedGraph, DbUtils graph, Node node){

        //right now it creates a new node
        //Make it place a pointer of the node to the new merged graph
        Label label = graph.getNodeLabel(node);
        HashMap<String, Object> props = graph.readNodeProperties(node); //takes O(k), k = number of properties
        if(mergedGraph.getNodeByID(props.get(Const.UUID)) == null){ // should take O(1) with hash table
            mergedGraph.createNewNodeInGraph(label, props); //should take O(1) but currently takes O(k)
        }
    }

    private static DbUtils computeUnionOfRelationships(DbUtils graph1, DbUtils graph2, DbUtils mergedGraph){
        /*
        Do the same with relationships as with the nodes
         */


        /* Computes the union of the relationships in the two graphs and puts the result in the merged graph. */

        ArrayList<String> allMergedGraphIds = mergedGraph.getAllIDs();
        for (Object mergedGraphId : allMergedGraphIds){
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
        //Test and handle merge conflicts with both graphs concerning the data in the nodes
        testAndHandleDataMergeConflict(graph1, graph2, mergedGraph);

        return mergedGraph;
    }
}