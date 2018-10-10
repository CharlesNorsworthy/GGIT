package VersionControl;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterable;

public class Merge {

    /**
     *
     * Created by cNorsworthy 9/27/18
     *
     * This class contains methods and helper methods for merging
     * two graphs together.
     *
     */

    GraphDatabaseService mergeMe(GraphDatabaseService graph1, GraphDatabaseService graph2){
        //This naive merge only works assuming no conflicts
        //Edges are NOT weighted

        GraphDatabaseService mergedGraph;
        mergedGraph.createNode();

        //Search through graph1 and graph2 (breadth first search) and get all nodes and what each node is connected to
        ResourceIterable<Node> graph1AllNodes = graph1.getAllNodes();
        ResourceIterable<Node> graph2AllNodes = graph2.getAllNodes();

        Node nextNode1, nextNode2;
        while(graph1AllNodes.iterator().hasNext() || graph2AllNodes.iterator().hasNext()){

            try{
                //Preemptively add the first node to graph 1.
                nextNode1 = graph1AllNodes.iterator().next();
                mergedGraph.add(nextNode1);
            }catch(Exception e){
                System.out.print("Out of nodes in Graph 1.");
            }

            try{
                //Only add the next node from graph 2 if it doesn't have the same id as the next node from graph 1.
                //So you won't add the same node twice.
                nextNode2 = graph2AllNodes.iterator().next();
                if(nextNode1.getID() != nextNode2.getID()){
                    mergedGraph.add(nextNode2);
                }
            }catch(Exception e){
                System.out.print("Out of nodes in Graph 2.");
            }
        }
        return mergedGraph;
    }
}