
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

        try(Transaction tx = mergedGraph.beginTx()){

            mergedGraph.createNode();

            //Search through graph1 and graph2 (breadth first search) and get all nodes and what each node is connected to
            ResourceIterable<Node> graph1AllNodes = graph1.getAllNodes();
            ResourceIterable<Node> graph2AllNodes = graph2.getAllNodes();

            Node nextNode1, nextNode2;
            while(graph1AllNodes.iterator().hasNext() || graph2AllNodes.iterator().hasNext()){

                try {
                    //Preemptively add the first node to graph 1.
                    nextNode1 = graph1AllNodes.iterator().next();
                    mergedGraph.createNode((Label) nextNode1);
                    //create relationship
                    nextNode1.getRelationships();

                    //Only add the next node from graph 2 if it doesn't have the same id as the next node from graph 1.
                    //So you won't add the same node twice.
                    nextNode2 = graph2AllNodes.iterator().next();
                    //create relationship
                    nextNode2.getRelationships();
                    if(nextNode1.getProperty("ID") != nextNode2.getProperty("ID")){
                        mergedGraph.createNode((Label) nextNode2);
                    }
                } catch(Exception ignored){}
            }
            tx.success();
        }
        return mergedGraph;
    }
}