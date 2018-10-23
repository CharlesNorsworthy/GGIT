import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.Iterator;

public class TestMerge {

    public static void main(String args[]){

        System.out.println("Testing current functionality with graph merging: ");
        GraphDatabaseService testGraph1 = createTestGraph1();
        GraphDatabaseService testGraph2 = createTestGraph2();

        GraphDatabaseService mergedGraph = Merge.mergeMe(testGraph1, testGraph2);
        System.out.println("Merged graph created with the following nodes: ");

        /* View the graph */
        try (ResourceIterator<Node> mergedGraphAllNodesIterator = DbUtils.getAllNodesIteratorStatic(mergedGraph)) {
            while (mergedGraphAllNodesIterator.hasNext()) {
                Node node = mergedGraphAllNodesIterator.next();
                System.out.println(DbUtils.getNodeID(mergedGraph, node));

                //TODO: print relationships after putting them in
                Iterator<Relationship> relsItr = DbUtils.getRelationshipIterator(mergedGraph, node);
                System.out.println(" Which has relationships to: ");
                while(relsItr.hasNext()){
                    Relationship rel = relsItr.next();
                    System.out.println(rel + " ");
                }
            }
        } catch (Exception e){
            System.out.println("Exception");
        }
    }

    private static GraphDatabaseService createTestGraph1(){

        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("\\C:\\TestGraph1"));
        try (Transaction tx = graphDb.beginTx()){
            Node firstNode;
            Node secondNode;
            Relationship relationship;

            firstNode = graphDb.createNode();
            firstNode.setProperty("ID", "1");
            secondNode = graphDb.createNode();
            secondNode.setProperty("ID", "2");

            //String id = DbUtils.getNodeID(graphDb, firstNode); //legal
            //String id = firstNode.getProperty("ID").toString(); //legal

            relationship = firstNode.createRelationshipTo(secondNode, DbUtils.RelTypes.KNOWS);
            relationship.setProperty( "message", "knows1" );

            System.out.println("Graph 1 created.");
            System.out.println("Node with ID = 1 created.");
            System.out.println("Node with ID = 2 created.");
            System.out.println("Relationship between node 1 and 2 created.");
            System.out.println();

            tx.success();
        }
        return graphDb;
    }

    private static GraphDatabaseService createTestGraph2(){

        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("\\C:\\TestGraph2"));
        try (Transaction tx = graphDb.beginTx()){
            Node firstNode;
            Node secondNode;
            Relationship relationship;

            firstNode = graphDb.createNode();
            firstNode.setProperty("ID", "1");
            secondNode = graphDb.createNode();
            secondNode.setProperty("ID", "3");

            relationship = firstNode.createRelationshipTo(secondNode, DbUtils.RelTypes.KNOWS);
            relationship.setProperty( "message", "knows2" );

            System.out.println("Graph 2 created.");
            System.out.println("Node with ID = 1 created.");
            System.out.println("Node with ID = 3 created.");
            System.out.println("Relationship between node 1 and 3 created.");
            System.out.println();

            tx.success();
        }
        return graphDb;
    }
}
