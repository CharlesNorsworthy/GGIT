package VersionControl;

import graphTool.Const;
import graphTool.DbUtils;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;

import java.util.Scanner;

/**
 * Defines a merge conflict exception with graph merging. A merge
 * conflict can either be conflicting data in the same node, or
 * conflicting addition/deletion in two graphs.
 *
 * @author Charles Norsworthy
 * @see VersionControl.Merge
 * @since 1.0
 */

public class MergeConflictException extends Exception {

    MergeConflictException(String s) {
        // Call constructor of parent Exception
        super(s);
    }

    /**
     * Handles a nodal merge conflict.
     *
     * @param mergedGraph the graph that the merge conflict was detected in
     * @param conflictingID the node that caused the merge conflict
     */

    MergeConflictException(DbUtils mergedGraph, String conflictingID){
        System.out.println("Resurfaced deleted node with id: "
                + conflictingID + ". Do you want to keep this node and its non-conflicting children? (Y/N)");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next().toLowerCase();

        Node conflictingNode = mergedGraph.getNodeByID(conflictingID);
        Label conflictingNodeLabel = mergedGraph.getNodeLabel(conflictingNode);
        if(input.equals("y")){
            System.out.println("Keeping node " + conflictingID + " and its non-conflicting children.");
        } else if (input.equals("n")){
            if(conflictingNodeLabel.equals(Const.ROOT_LABEL)){
                System.out.println("You cannot delete the root node.");
            } else {
                System.out.println("Deleting node " + conflictingID + " and its children.");
                mergedGraph.deleteNode(conflictingNodeLabel, conflictingID);
            }
        }
    }

    /**
     * Handles a merge conflict concerning the data in a node.
     *
     * @param mergedGraph the graph that the merge conflict was detected in
     * @param conflictingId the id of the node with conflicting data
     * @param key the data type of the conflicting data
     * @param graph1Property original graph data
     * @param graph2Property merged graph data
     */

    MergeConflictException(DbUtils mergedGraph, String conflictingId, String key,
                           String graph1Property, String graph2Property){
        System.out.println("Conflicting data on node with id: "
                + conflictingId + "of data type " + key + ". Which data would you rather keep?");
        System.out.println("A. Original graph data: " + graph1Property);
        System.out.println("B. Merged graph data: " + graph2Property);
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next().toLowerCase();

        Node conflictingNode = mergedGraph.getNodeByID(conflictingId);
        if(input.equals("a")){
            mergedGraph.setProperty(conflictingNode, key, graph1Property);
        } else if (input.equals("b")){
            mergedGraph.setProperty(conflictingNode, key, graph2Property);
        }
    }
}