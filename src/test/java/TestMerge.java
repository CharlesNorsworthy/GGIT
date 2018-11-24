import VersionControl.Merge;
import graphTool.DbOps;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class TestMerge {

    public static void main(String args[]){

        System.out.println("Testing current functionality with graph merging: ");
        DbOps testGraph1 = createTestGraph1();
        DbOps testGraph2 = createTestGraph2();
        DbOps mergedGraph = new DbOps("\\C:\\databases\\MergedGraph");
        Merge.mergeNaively(testGraph1, testGraph2, mergedGraph);

//        System.out.println("Merged graph created with the following nodes: ");
//
//        /* View the graph */
//        try (ResourceIterator<Node> mergedGraphAllNodesIterator = MergeUtils.getAllNodesIteratorStatic(mergedGraph)) {
//            while (mergedGraphAllNodesIterator.hasNext()) {
//                Node node = mergedGraphAllNodesIterator.next();
//                System.out.print(MergeUtils.getNodeID(mergedGraph, node));
//
//                Iterator<Relationship> relsItr = MergeUtils.getRelationshipIterator(mergedGraph, node);
//                System.out.println(" , which has relationships: ");
//                while(relsItr.hasNext()){
//                    Relationship rel = relsItr.next();
//                    System.out.println(rel + " ");
//                }
//            }
//        } catch (Exception e){
//            System.out.println("Exception");
//        }
    }

    private static DbOps createTestGraph1(){

        DbOps graphDb = new DbOps("\\C:\\databases\\TestGraph1");
        Node rootNode;
        Node node1;
        Node node3;
        Node node2;
        Relationship relationship1;
        Relationship relationship2;
        Relationship relationship3;

        return graphDb;
    }

    private static DbOps createTestGraph2(){

        DbOps graphDb = new DbOps("\\C:\\databases\\TestGraph2");
        Node rootNode;
        Node node1;
        Node node3;
        Node node4;
        Relationship relationship1;
        Relationship relationship2;
        Relationship relationship3;
        Relationship relationship4;

        return graphDb;
    }
}
