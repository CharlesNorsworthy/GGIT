package VersionControl;

/**
 * Defines a merge conflict exception with graph merging.
 *
 * A merge conflict can either be conflicting data in the same node,
 * or conflicting addition/deletion in two graphs
 *
 */
//TODO: try to incorporate
class MergeConflictException extends Exception {

    MergeConflictException(String s) {
        // Call constructor of parent Exception
        super(s);
    }
}