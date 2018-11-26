import VersionControl.Merge;
import graphTool.Const;
import graphTool.DbOps;
import graphTool.DbUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class TestMerge {

    public static void main(String args[]){
        System.out.println("Testing current functionality with graph merging: ");
        //testNaiveMerge1();
        //testNaiveMerge2();
        //testNaiveMerge3();
        testMergeWithConflicts1();

    }

    private static void testNaiveMerge1(){
        //This merge has an emphasis on newly added nodes
        DbOps testGraph1 = createGraph1ForNaiveMerge1();
        DbOps testGraph2 = createGraph2ForNaiveMerge1();
        DbUtils mergedGraph = new DbUtils("\\C:\\databases\\NaiveTest1MergedGraph");
        Merge.mergeNaively(testGraph1.getDb(), testGraph2.getDb(), mergedGraph);
    }

    private static void testNaiveMerge2(){
        //This merge has an emphasis on newly added relationships, includes
        DbOps testGraph1 = createGraph1ForNaiveMerge2();
        DbOps testGraph2 = createGraph2ForNaiveMerge2();
        DbUtils mergedGraph = new DbUtils("\\C:\\databases\\NaiveTest2MergedGraph");
        Merge.mergeNaively(testGraph1.getDb(), testGraph2.getDb(), mergedGraph);
    }

    private static void testNaiveMerge3(){
        //This merge has an emphasis on properties to test data merging
        DbOps testGraph1 = createGraph1ForNaiveMerge3();
        DbOps testGraph2 = createGraph2ForNaiveMerge3();
        DbUtils mergedGraph = new DbUtils("\\C:\\databases\\NaiveTest3MergedGraph");
        Merge.mergeNaively(testGraph1.getDb(), testGraph2.getDb(), mergedGraph);
    }

    private static DbOps createGraph1ForNaiveMerge3(){
        DbOps graphDb = new DbOps("\\C:\\databases\\NaiveTest3Graph1", "0");

        HashMap<String, Object> node1Props = new HashMap<>();
        HashMap<String, Object> node2Props = new HashMap<>();
        node1Props.put(Const.UUID, "1");
        node1Props.put(Const.NAME, "Nicholas");
        node2Props.put(Const.UUID, "2");
        node2Props.put(Const.NAME, "Moran");

        graphDb.createObservation(node1Props);
        graphDb.createObservation(node2Props);

        return graphDb;
    }

    private static DbOps createGraph2ForNaiveMerge3(){
        DbOps graphDb = new DbOps("\\C:\\databases\\NaiveTest3Graph2", "0");

        HashMap<String, Object> node1Props = new HashMap<>();
        HashMap<String, Object> node2Props = new HashMap<>();
        node1Props.put(Const.UUID, "1");
        node1Props.put(Const.NAME, "Allison");
        node2Props.put(Const.UUID, "2");
        node2Props.put(Const.NAME, "Whitehead");

        graphDb.createObservation(node1Props);
        graphDb.createObservation(node2Props);

        return graphDb;
    }

    private static void testMergeWithConflicts1(){
        DbOps ancestorGraph = createAncestorGraphForConflictMerge1();
        DbOps testGraph1 = createGraph1ForConflictMerge1();
        DbOps testGraph2 = createGraph2ForConflictMerge1();
        DbUtils mergedGraph1 = new DbUtils("\\C:\\databases\\ConflictTest1MergedGraph1");
        DbUtils mergedGraph2 = new DbUtils("\\C:\\databases\\ConflictTest1MergedGraph2");
        Merge.mergeWithPossibleConflicts(testGraph1.getDb(), testGraph2.getDb(), ancestorGraph.getDb(), mergedGraph1);
        Merge.mergeWithPossibleConflicts(testGraph1.getDb(), testGraph2.getDb(), ancestorGraph.getDb(), mergedGraph2);
    }

    private static DbOps createAncestorGraphForConflictMerge1(){
        DbOps graphDb = new DbOps("\\C:\\databases\\ConflictTest1Ancestor", "0");

        HashMap<String, Object> node1Props = new HashMap<>();
        node1Props.put(Const.UUID, "1");

        graphDb.createObservation(node1Props);

        return graphDb;
    }

    private static DbOps createGraph1ForConflictMerge1(){
        DbOps graphDb = new DbOps("\\C:\\databases\\ConflictTest1Graph1", "0");

        HashMap<String, Object> node1Props = new HashMap<>();
        HashMap<String, Object> node2Props = new HashMap<>();
        node1Props.put(Const.UUID, "1");
        node2Props.put(Const.UUID, "2");

        graphDb.createObservation(node1Props);
        graphDb.createKnowledge("1", node2Props);

        return graphDb;
    }

    private static DbOps createGraph2ForConflictMerge1(){
        DbOps graphDb = new DbOps("\\C:\\databases\\ConflictTest1Graph2", "0");
        HashMap<String, Object> node3Props = new HashMap<>();
        node3Props.put(Const.UUID, "3");

        graphDb.createObservation(node3Props);

        return graphDb;
    }

    private static void testMergeWithConflicts2(){
        DbOps ancestorGraph = createAncestorGraphForConflictMerge2();
        DbOps testGraph1 = createGraph1ForConflictMerge2();
        DbOps testGraph2 = createGraph2ForConflictMerge2();
        DbUtils mergedGraph1 = new DbUtils("\\C:\\databases\\ConflictTest2MergedGraph1");
        DbUtils mergedGraph2 = new DbUtils("\\C:\\databases\\ConflictTest2MergedGraph2");
        Merge.mergeWithPossibleConflicts(testGraph1.getDb(), testGraph2.getDb(), ancestorGraph.getDb(), mergedGraph1);
        Merge.mergeWithPossibleConflicts(testGraph1.getDb(), testGraph2.getDb(), ancestorGraph.getDb(), mergedGraph2);
    }

    private static DbOps createGraph1ForNaiveMerge1(){

        DbOps graphDb = new DbOps("\\C:\\databases\\NaiveTest1Graph1", "0");

        HashMap<String, Object> node1Props = new HashMap<>();
        HashMap<String, Object> node2Props = new HashMap<>();
        HashMap<String, Object> node3Props = new HashMap<>();
        HashMap<String, Object> node4Props = new HashMap<>();
        node1Props.put(Const.UUID, "1");
        node2Props.put(Const.UUID, "2");
        node3Props.put(Const.UUID, "3");
        node4Props.put(Const.UUID, "4");

        graphDb.createObservation(node1Props);
        graphDb.createObservation(node2Props);

        graphDb.createKnowledge("1", node3Props);
        graphDb.createKnowledge("2", node4Props);

        return graphDb;
    }

    private static DbOps createGraph2ForNaiveMerge1(){
        DbOps graphDb = new DbOps("\\C:\\databases\\NaiveTest1Graph2", "0");

        HashMap<String, Object> node1Props = new HashMap<>();
        HashMap<String, Object> node5Props = new HashMap<>();
        HashMap<String, Object> node6Props = new HashMap<>();
        node1Props.put(Const.UUID, "1");
        node5Props.put(Const.UUID, "5");
        node6Props.put(Const.UUID, "6");

        graphDb.createObservation(node1Props);
        graphDb.createObservation(node5Props);

        graphDb.createKnowledge("1", node6Props);

        return graphDb;
    }

    private static DbOps createGraph1ForNaiveMerge2(){
        DbOps graphDb = new DbOps("\\C:\\databases\\NaiveTest2Graph1", "0");

        HashMap<String, Object> node1Props = new HashMap<>();
        HashMap<String, Object> node2Props = new HashMap<>();
        HashMap<String, Object> node3Props = new HashMap<>();
        HashMap<String, Object> node4Props = new HashMap<>();
        HashMap<String, Object> node5Props = new HashMap<>();
        node1Props.put(Const.UUID, "1");
        node2Props.put(Const.UUID, "2");
        node3Props.put(Const.UUID, "3");
        node4Props.put(Const.UUID, "4");
        node5Props.put(Const.UUID, "5");

        graphDb.createObservation(node1Props);
        graphDb.createObservation(node2Props);
        graphDb.createObservation(node3Props);

        ArrayList<String> obsIds = new ArrayList<>();
        obsIds.add("1");
        obsIds.add("2");
        graphDb.createKnowledge(obsIds, node4Props);
        graphDb.createKnowledge("3", node5Props);


        return graphDb;
    }

    private static DbOps createGraph2ForNaiveMerge2(){
        DbOps graphDb = new DbOps("\\C:\\databases\\NaiveTest2Graph2", "0");

        HashMap<String, Object> node1Props = new HashMap<>();
        HashMap<String, Object> node2Props = new HashMap<>();
        HashMap<String, Object> node3Props = new HashMap<>();
        HashMap<String, Object> node4Props = new HashMap<>();
        HashMap<String, Object> node5Props = new HashMap<>();
        node1Props.put(Const.UUID, "1");
        node2Props.put(Const.UUID, "2");
        node3Props.put(Const.UUID, "3");
        node4Props.put(Const.UUID, "4");
        node5Props.put(Const.UUID, "5");

        graphDb.createObservation(node1Props);
        graphDb.createObservation(node2Props);
        graphDb.createObservation(node3Props);

        graphDb.createKnowledge("1", node4Props);
        graphDb.createKnowledge("2", node5Props);


        return graphDb;
    }

    private static DbOps createAncestorGraphForConflictMerge2(){
        return null;
    }

    private static DbOps createGraph1ForConflictMerge2(){
        return null;
    }

    private static DbOps createGraph2ForConflictMerge2(){
        return null;
    }
}