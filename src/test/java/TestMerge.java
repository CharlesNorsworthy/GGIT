import VersionControl.Merge;
import VersionControl.MergeUtils;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.Iterator;

public class TestMerge {

    public static void main(String args[]){
        //TODO: create graphs using DbOps

        System.out.println("Testing current functionality with graph merging: ");
        GraphDatabaseService testGraph1 = createTestGraph1();
        GraphDatabaseService testGraph2 = createTestGraph2();
        GraphDatabaseService mergedGraph = new GraphDatabaseFactory().newEmbeddedDatabase(new File("\\C:\\databases\\MergedGraph"));
        mergedGraph = Merge.mergeMe(testGraph1, testGraph2, mergedGraph);

        System.out.println("Merged graph created with the following nodes: ");

        /* View the graph */
        try (ResourceIterator<Node> mergedGraphAllNodesIterator = MergeUtils.getAllNodesIteratorStatic(mergedGraph)) {
            while (mergedGraphAllNodesIterator.hasNext()) {
                Node node = mergedGraphAllNodesIterator.next();
                System.out.print(MergeUtils.getNodeID(mergedGraph, node));

                Iterator<Relationship> relsItr = MergeUtils.getRelationshipIterator(mergedGraph, node);
                System.out.println(" , which has relationships: ");
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

        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("\\C:\\databases\\TestGraph1"));
        try (Transaction tx = graphDb.beginTx()){
            Node firstNode;
            Node secondNode;
            Node thirdNode;
            Node fourthNode;
            Relationship relationship1;
            Relationship relationship2;

            firstNode = graphDb.createNode();
            firstNode.setProperty("ID", "0");
            secondNode = graphDb.createNode();
            secondNode.setProperty("ID", "1");
            thirdNode = graphDb.createNode();
            thirdNode.setProperty("ID", "3");

            relationship1 = firstNode.createRelationshipTo(secondNode, MergeUtils.RelTypes.KNOWS);
            relationship1.setProperty("message", "knows1");
            relationship2 = secondNode.createRelationshipTo(thirdNode, MergeUtils.RelTypes.KNOWS);
            relationship2.setProperty("message", "knows2");

            System.out.println("Graph 1 created.");
            System.out.println("Node with ID = 1 created.");
            System.out.println("Node with ID = 2 created.");
            System.out.println("Relationship between node 1 and 2 created.");
            System.out.println();

            Iterator<Relationship> relsItr = MergeUtils.getRelationshipIterator(graphDb, firstNode);
            Iterator<Relationship> relsItr2 = MergeUtils.getRelationshipIterator(graphDb, secondNode);
            Iterator<Relationship> relsItr3 = MergeUtils.getRelationshipIterator(graphDb, thirdNode);
            System.out.println(" , which has relationships: ");
            while(relsItr.hasNext()){
                Relationship rel = relsItr.next();
                System.out.println(rel + " ");
            }
            while(relsItr2.hasNext()){
                Relationship rel = relsItr2.next();
                System.out.println(rel + " ");
            }
            while(relsItr3.hasNext()){
                Relationship rel = relsItr3.next();
                System.out.println(rel + " ");
            }
            tx.success();
        }
        return graphDb;
    }

//    private static GraphDatabaseService createTestGraph2(){
//
//        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("\\C:\\databases\\TestGraph2"));
//        try (Transaction tx = graphDb.beginTx()){
//            Node firstNode;
//            Node secondNode;
//            Relationship relationship;
//
//            firstNode = graphDb.createNode();
//            firstNode.setProperty("ID", "1");
//            secondNode = graphDb.createNode();
//            secondNode.setProperty("ID", "4");
//
//            relationship = firstNode.createRelationshipTo(secondNode, MergeUtils.RelTypes.KNOWS);
//            relationship.setProperty( "message", "knows2" );
//
//            System.out.println("Graph 2 created.");
//            System.out.println("Node with ID = 1 created.");
//            System.out.println("Node with ID = 3 created.");
//            System.out.println("Relationship between node 1 and 3 created.");
//            System.out.println();
//
//            tx.success();
//        }
//        return graphDb;
//    }

    private static GraphDatabaseService createTestGraph2(){

        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("\\C:\\databases\\TestGraph2"));
        try (Transaction tx = graphDb.beginTx()){
            Node firstNode;
            Node secondNode;
            Node thirdNode;
            Relationship relationship1;
            Relationship relationship2;

            firstNode = graphDb.createNode();
            firstNode.setProperty("ID", "1");
            secondNode = graphDb.createNode();
            secondNode.setProperty("ID", "3");
            thirdNode = graphDb.createNode();
            thirdNode.setProperty("ID", "4");

            relationship1 = firstNode.createRelationshipTo(secondNode, MergeUtils.RelTypes.KNOWS);
            relationship1.setProperty("message", "knows1");
            relationship2 = secondNode.createRelationshipTo(thirdNode, MergeUtils.RelTypes.KNOWS);
            relationship2.setProperty("message", "knows2");

            System.out.println("Graph 1 created.");
            System.out.println("Node with ID = 1 created.");
            System.out.println("Node with ID = 2 created.");
            System.out.println("Relationship between node 1 and 2 created.");
            System.out.println();

            Iterator<Relationship> relsItr = MergeUtils.getRelationshipIterator(graphDb, firstNode);
            Iterator<Relationship> relsItr2 = MergeUtils.getRelationshipIterator(graphDb, secondNode);
            Iterator<Relationship> relsItr3 = MergeUtils.getRelationshipIterator(graphDb, thirdNode);
            System.out.println(" , which has relationships: ");
            while(relsItr.hasNext()){
                Relationship rel = relsItr.next();
                System.out.println(rel + " ");
            }
            while(relsItr2.hasNext()){
                Relationship rel = relsItr2.next();
                System.out.println(rel + " ");
            }
            while(relsItr3.hasNext()){
                Relationship rel = relsItr3.next();
                System.out.println(rel + " ");
            }
            tx.success();
        }
        return graphDb;
    }
}
