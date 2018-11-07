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
            Node rootNode;
            Node node1;
            Node node3;
            Node node2;
            Relationship relationship1;
            Relationship relationship2;
            Relationship relationship3;

            rootNode = graphDb.createNode();
            rootNode.setProperty("ID", "0");
            node1 = graphDb.createNode();
            node1.setProperty("ID", "1");
            node3 = graphDb.createNode();
            node3.setProperty("ID", "3");
            node2 = graphDb.createNode();
            node2.setProperty("ID", "2");

            relationship1 = rootNode.createRelationshipTo(node1, MergeUtils.RelTypes.KNOWS);
            relationship1.setProperty("message", "knows1");
            relationship2 = node1.createRelationshipTo(node2, MergeUtils.RelTypes.KNOWS);
            relationship2.setProperty("message", "knows2");
            relationship3 = rootNode.createRelationshipTo(node3, MergeUtils.RelTypes.KNOWS);

            System.out.println("Graph 1 created.");
            System.out.println("Node with ID = 1 created.");
            System.out.println("Node with ID = 2 created.");
            System.out.println("Relationship between node 1 and 2 created.");
            System.out.println();

            Iterator<Relationship> relsItr = MergeUtils.getRelationshipIterator(graphDb, rootNode);
            Iterator<Relationship> relsItr2 = MergeUtils.getRelationshipIterator(graphDb, node1);
            Iterator<Relationship> relsItr3 = MergeUtils.getRelationshipIterator(graphDb, node3);
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

    private static GraphDatabaseService createTestGraph2(){

        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("\\C:\\databases\\TestGraph2"));
        try (Transaction tx = graphDb.beginTx()){
            Node rootNode;
            Node node1;
            Node node3;
            Node node4;
            Relationship relationship1;
            Relationship relationship2;
            Relationship relationship3;
            Relationship relationship4;

            rootNode = graphDb.createNode();
            rootNode.setProperty("ID", "0");
            node1 = graphDb.createNode();
            node1.setProperty("ID", "1");
            node3 = graphDb.createNode();
            node3.setProperty("ID", "3");
            node4 = graphDb.createNode();
            node4.setProperty("ID", "4");

            relationship1 = rootNode.createRelationshipTo(node1, MergeUtils.RelTypes.KNOWS);
            relationship1.setProperty("message", "knows1");
            relationship2 = rootNode.createRelationshipTo(node3, MergeUtils.RelTypes.KNOWS);
            relationship2.setProperty("message", "knows2");
            relationship3 = node1.createRelationshipTo(node4, MergeUtils.RelTypes.KNOWS);
            relationship4 = node3.createRelationshipTo(node4, MergeUtils.RelTypes.KNOWS);

            System.out.println("Graph 1 created.");
            System.out.println("Node with ID = 1 created.");
            System.out.println("Node with ID = 2 created.");
            System.out.println("Relationship between node 1 and 2 created.");
            System.out.println();

            Iterator<Relationship> relsItr = MergeUtils.getRelationshipIterator(graphDb, rootNode);
            Iterator<Relationship> relsItr2 = MergeUtils.getRelationshipIterator(graphDb, node1);
            Iterator<Relationship> relsItr3 = MergeUtils.getRelationshipIterator(graphDb, node3);
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
