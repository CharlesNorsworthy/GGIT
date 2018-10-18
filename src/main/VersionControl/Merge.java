
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

class Merge {

    /**
     *
     * Created by cNorsworthy 9/27/18
     *
     * This class contains methods and helper methods for merging
     * two graphs together.
     *
     */

    static GraphDatabaseService mergeMe(GraphDatabaseService graph1, GraphDatabaseService graph2){
        //We are actually merging two graph databases by adding their nodes into another graph database
        //This naive merge only works assuming no conflicts
        //Edges are NOT weighted

        GraphDatabaseService mergedGraph = new GraphDatabaseFactory().newEmbeddedDatabase(new File("\\C:\\MergedGraph"));

        //Search through graph1 and graph2 (breadth first search) and get all nodes and what each node is connected to
        try (ResourceIterator<Node> graph1AllNodesIterator = DbUtils.getAllNodesIterator(graph1); ResourceIterator<Node> graph2AllNodesIterator = DbUtils.getAllNodesIterator(graph2)) {
            //Node nextNode1, nextNode2;
            while (graph1AllNodesIterator.hasNext() || graph1AllNodesIterator.hasNext()) {

                //Preemptively add the first node to graph 1.
                Node nextNode1 = graph1AllNodesIterator.next();
                DbUtils.putNodeInGraph(mergedGraph, nextNode1);

                //Only add the next node from graph 2 if it doesn't have the same id as the next node from graph 1.
                //So you won't add the same node twice.
                Node nextNode2 = graph2AllNodesIterator.next();

                //If the ids of the next nodes aren't equal, put in the next node from the second graph
                if (!DbUtils.getNodeID(graph1, nextNode1).equals(DbUtils.getNodeID(graph2, nextNode2))) {
                    DbUtils.putNodeInGraph(mergedGraph, nextNode2);
                }
            }
        }

    return mergedGraph;
    }
}