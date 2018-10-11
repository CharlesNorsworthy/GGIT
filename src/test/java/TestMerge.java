import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

public class TestMerge {

    public static void main(String args[]) {

        GraphDatabaseService testGraph1 = createTestGraph1();
        GraphDatabaseService testGraph2 = createTestGraph2();
        GraphDatabaseService mergedGraph = Merge.mergeMe(testGraph1, testGraph2);

        /* View the graph */
        ResourceIterable<Node> mergedGraphAllNodes = mergedGraph.getAllNodes();
        for(Node node: mergedGraphAllNodes){
            System.out.println(node);
            System.out.println(node.getRelationships());
            System.out.println("--------------------------------------");
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

            relationship = firstNode.createRelationshipTo(secondNode, DbUtils.RelTypes.KNOWS);
            relationship.setProperty( "message", "knows1" );

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

            tx.success();
        }
        return graphDb;
    }
}
