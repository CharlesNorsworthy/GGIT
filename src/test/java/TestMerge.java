import VersionControl.Merge;
import graphTool.Const;
import graphTool.DbOps;

import java.util.HashMap;

public class TestMerge {

    public static void main(String args[]){
        System.out.println("Testing current functionality with graph merging: ");
        //testNaiveMerge1();
        createGraph1ForNaiveMerge1();
    }

    private static void testNaiveMerge1(){
        DbOps testGraph1 = createGraph1ForNaiveMerge1();
        DbOps testGraph2 = createGraph2ForNaiveMerge1();
        DbOps mergedGraph = new DbOps("\\C:\\databases\\NaiveTest1MergedGraph", "0");
        Merge.mergeNaively(testGraph1, testGraph2, mergedGraph);
    }

    private static void testNaiveMerge2(){
        DbOps testGraph1 = createGraph1ForNaiveMerge2();
        DbOps testGraph2 = createGraph2ForNaiveMerge2();
        DbOps mergedGraph = new DbOps("\\C:\\databases\\NaiveTest2MergedGraph");
        Merge.mergeNaively(testGraph1, testGraph2, mergedGraph);
    }

    private static void testMergeWithConflicts1(){
        DbOps ancestorGraph = createAncestorGraphForConflictMerge1();
        DbOps testGraph1 = createGraph1ForConflictMerge1();
        DbOps testGraph2 = createGraph2ForConflictMerge1();
        DbOps mergedGraph1 = new DbOps("\\C:\\databases\\ConflictTest1MergedGraph1");
        DbOps mergedGraph2 = new DbOps("\\C:\\databases\\ConflictTest1MergedGraph2");
        Merge.mergeWithPossibleConflicts(testGraph1, testGraph2, ancestorGraph, mergedGraph1);
        Merge.mergeWithPossibleConflicts(testGraph1, testGraph2, ancestorGraph, mergedGraph2);
    }

    private static void testMergeWithConflicts2(){
        DbOps ancestorGraph = createAncestorGraphForConflictMerge2();
        DbOps testGraph1 = createGraph1ForConflictMerge2();
        DbOps testGraph2 = createGraph2ForConflictMerge2();
        DbOps mergedGraph1 = new DbOps("\\C:\\databases\\ConflictTest2MergedGraph1");
        DbOps mergedGraph2 = new DbOps("\\C:\\databases\\ConflictTest2MergedGraph2");
        Merge.mergeWithPossibleConflicts(testGraph1, testGraph2, ancestorGraph, mergedGraph1);
        Merge.mergeWithPossibleConflicts(testGraph1, testGraph2, ancestorGraph, mergedGraph2);
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
        return null;
    }

    private static DbOps createGraph2ForNaiveMerge2(){
        return null;
    }

    private static DbOps createAncestorGraphForConflictMerge1(){
        return null;
    }

    private static DbOps createGraph1ForConflictMerge1(){
        return null;
    }

    private static DbOps createGraph2ForConflictMerge1(){
        return null;
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