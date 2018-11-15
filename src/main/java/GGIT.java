import graphTool.Const;
import graphTool.DbUtils;

/** GGIT is the driver class for this Graph Database Version Control System
 * Command line input drives this class
 */
public class GGIT {
    private static GGITNode currentNode;

    public static void main(String args[]) {
//        GraphDriver graphDriver = new GraphDriver();
//        graphDriver.Drive();
//        DbUtils db = new DbUtils("\\C:\\Neo4J");
//        db.initRoot();
//        db.createDefaultNodes();
//        db.deleteNodes(Const.OBSERVATION_LABEL);
//        db.deleteNodes(Const.KNOWLEDGE_LABEL);
//        db.deleteNodes(Const.ROOT_LABEL);
//        db.readNodes(Const.OBSERVATION_LABEL);
    }

    private static void executeCmd(String args[]){
        try {
            String input = args[0];
            switch (input) {
                case "init":
                    _init(args);
                    break;
                case "clone":
                    _clone(args);
                    break;
                case "add":
                    _add(args);
                    break;
                case "commit":
                    _commit(args);
                    break;
                case "push":
                    _push(args);
                    break;
                case "status":
                    _status(args);
                    break;
                case "remote":
                    _remote(args);
                    break;
                case "checkout":
                    _checkout(args);
                    break;
                case "branch":
                    _branch(args);
                    break;
                case "pull":
                    _pull(args);
                    break;
                case "merge":
                    _merge(args);
                    break;
                case "diff":
                    _diff(args);
                    break;
                case "log":
                    _log(args);
                    break;
                case "fetch":
                    _fetch(args);
                    break;
                case "reset":
                    _reset(args);
                    break;
                case "grep":
                    _grep(args);
                    break;
                default:
                    System.out.println("The user must enter a command");
            }
        } catch (UnsupportedOperationException e) {
            System.out.println(e);
        }
    }

    private static void _init(String[] args) {
        throw new UnsupportedOperationException("The " + args[0] + " command is not currently supported.");
    }

    private static void _clone(String[] args) {
        throw new UnsupportedOperationException("The " + args[0] + " command is not currently supported.");
    }

    private static void _add(String[] args) {
        throw new UnsupportedOperationException("The " + args[0] + " command is not currently supported.");
    }

    private static void _commit(String[] args) {
        throw new UnsupportedOperationException("The " + args[0] + " command is not currently supported.");
    }

    private static void _push(String[] args) {
        throw new UnsupportedOperationException("The " + args[0] + " command is not currently supported.");
    }

    private static void _status(String[] args) {
        throw new UnsupportedOperationException("The " + args[0] + " command is not currently supported.");
    }

    private static void _remote(String[] args) {
        throw new UnsupportedOperationException("The " + args[0] + " command is not currently supported.");
    }

    private static void _checkout(String[] args) {
        throw new UnsupportedOperationException("The " + args[0] + " command is not currently supported.");
    }

    private static void _branch(String[] args) {
        throw new UnsupportedOperationException("The " + args[0] + " command is not currently supported.");
    }

    private static void _pull(String[] args) {
        throw new UnsupportedOperationException("The " + args[0] + " command is not currently supported.");
    }

    private static void _merge(String[] args) {
        throw new UnsupportedOperationException("The " + args[0] + " command is not currently supported.");
    }

    private static void _diff(String[] args) {
        throw new UnsupportedOperationException("The " + args[0] + " command is not currently supported.");
    }

    private static void _log(String[] args) {
        throw new UnsupportedOperationException("The " + args[0] + " command is not currently supported.");
    }

    private static void _fetch(String[] args) {
        throw new UnsupportedOperationException("The " + args[0] + " command is not currently supported.");
    }

    private static void _reset(String[] args) {
        throw new UnsupportedOperationException("The " + args[0] + " command is not currently supported.");
    }

    private static void _grep(String[] args) {
        throw new UnsupportedOperationException("The " + args[0] + " command is not currently supported.");
    }
}
