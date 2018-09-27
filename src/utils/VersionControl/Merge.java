package VersionControl;

public class GraphMerge {

    /**
     *
     * Created by cNorsworthy 9/27/18
     *
     * This class contains methods for merging two graphs together.
     *
     */

    Graph mergeMe(Graph g1, Graph g2){
        //This naive merge only works assuming no conflicts
        //Edges are NOT weighted

        Graph mergedGraph = new Graph();

        //Search through g1 and g2 and get all nodes and what each node is connected to
        g1.getAllNodesAndEdges();
        g2.getAllNodesAndEdges();

        Node nextNode1, nextNode2;

        while(g1.hasNextNode() || g2.hasnextNode()){

            try{
                nextNode1 = g1.getNextNode();
                mergedGraph.add(nextNode1);
            }catch(Exception e){
                System.out.print("Out of nodes in Graph 1.");
            }

            try{
                nextNode1 = g1.getNextNode();
                if(nextNode1.getID() != nextNode2.getID()){
                    mergedGraph.add(nextNode1);
                }
            }catch(Exception e){
                System.out.print("Out of nodes in Graph 2.");
            }
        }
        return mergedGraph;
    }
}