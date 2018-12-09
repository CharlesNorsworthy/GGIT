package VersionControl;

import graphTool.Const;
import graphTool.DbUtils;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

//TODO: eventually rename to just Merge after its done
public class OptimizedMerge {

    /**
     * This merge computes the union of two graphs by computing the union of their edge
     * and vertices sets. This can be accomplished in O(max(|e1|*k, |e2|*k) + max(|v1|, |v2|))
     * time, where e1, e2 and v1, v2 are the edge (node) and vertex (relationship) sets
     * of graph1 and graph2 respectively. The cardinality, or number of elements in a set s,
     * is denoted |s|. The number k is the number of properties in a node.
     *
     * @param graph1
     * @param graph2
     * @param mergedGraph
     * @return DbUtils
     */

    public static DbUtils naiveMerge(DbUtils graph1, DbUtils graph2, DbUtils mergedGraph){

        mergedGraph = computeUnionOfNodes(graph1, graph2, mergedGraph);

        mergedGraph = computeUnionOfRelationships(graph1, graph2, mergedGraph);

        return mergedGraph;
    }

    private static DbUtils computeUnionOfNodes(DbUtils graph1, DbUtils graph2, DbUtils mergedGraph){
        /* Computes the union of the nodes in the two graphs and puts the result in the merged graph. */
        //TODO: optimize and refactor

        //Get all nodes from both graphs
        try (ResourceIterator<Node> graph1AllNodesIterator = graph1.getAllNodesIterator();
             ResourceIterator<Node> graph2AllNodesIterator = graph2.getAllNodesIterator()) {

            /* Put the nodes in the merged graph */
            while (graph1AllNodesIterator.hasNext() || graph2AllNodesIterator.hasNext()) {
                //assume the nodes do not exist unless shown otherwise
                Node nextNode1 = null;
                Node nextNode2 = null;
                try{
                    nextNode1 = graph1AllNodesIterator.next();
                    //Preemptively add the first node from graph 1.
                    if(nextNode1 != null){
                        Label label1 = graph1.getNodeLabel(nextNode1);
                        HashMap<String, Object> props1 = graph1.readNodeProperties(nextNode1);
                        if(mergedGraph.getNodeByID(props1.get(Const.UUID)) == null){
                            mergedGraph.putNodeInGraph(label1, props1);
                        }

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
                        Label label2 = graph2.getNodeLabel(nextNode2);
                        HashMap<String, Object> props2 = graph2.readNodeProperties(nextNode2);
                        if(mergedGraph.getNodeByID(props2.get(Const.UUID)) == null) {
                            mergedGraph.putNodeInGraph(label2, props2);
                        }
                    }
                } else if (nextNode2 != null){
                    Label label2 = graph2.getNodeLabel(nextNode2);
                    HashMap<String, Object> props2 = graph2.readNodeProperties(nextNode2);
                    if(mergedGraph.getNodeByID(props2.get(Const.UUID)) == null) {
                        mergedGraph.putNodeInGraph(label2, props2);
                    }
                }
            }
        }
        return mergedGraph;
    }

    private static DbUtils computeUnionOfRelationships(DbUtils graph1, DbUtils graph2, DbUtils mergedGraph){
        /* Computes the union of the relationships in the two graphs and puts the result in the merged graph. */

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
        //Test and handle merge conflicts with both graphs concerning the data in the nodes
        testAndHandleDataMergeConflict(graph1, graph2, mergedGraph);

        return mergedGraph;
    }
}