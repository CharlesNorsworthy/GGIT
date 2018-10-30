import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

public class TestTransactions {

    public static void main(String[] args){
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("\\C:\\TestGraph1"));

    }

    static String getNodeID(GraphDatabaseService graph, Node node){
        String ID;
        try(Transaction tx = graph.beginTx()){
            Object ID1 = node.getProperty("ID");
            ID = ID1.toString();
            //ID = String.valueOf(node.getId());
            tx.success();
        }
        return ID;
    }

}
