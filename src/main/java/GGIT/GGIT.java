package GGIT;

import java.io.*;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

/** GGIT.GGIT is the driver class for this Graph Database Version Control System
 * Command line input drives this class
 */
public class GGIT {
    private static GGITGraph repo;

    private static GGITNode currentNode;

    private static Options options;

    public static void main(String args[]) {
        if (checkDataStore()) {
            deserialize();
        }
        options = GGITConst.OPTIONS;
        executeCmd(args);
    }

    private static void executeCmd(String args[]) {
        try {
            if (args.length > 0) {
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
                        _no_command();
                }
            } else {
                _no_command();
            }
        } catch (UnsupportedOperationException e) {
            System.out.println("UnsupportedOperationException :: " + e);
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException :: " +e);
        } finally {
            serialize();
        }
    }

    private static void _init(String[] args) {
        if (repo == null) {
            String graphRef = "C://Neo4j//graph.db";
            if (args.length > 1) {
                graphRef = args[1];
            }
            GGITGraph repo = new GGITGraph(graphRef, GGITConst.MASTER);
            if (repo != null) {
                System.out.println("A repo was successfully initialized!");
            } else {
                System.out.println("FAILED to initialize a repo.");
            }
        } else {
            throw new IllegalArgumentException("There is already a repository that exists.");
        }
    }

    private static void _clone(String[] args) {
        String graphRef;
        if (args.length > 1) {

        }
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

    private static void _no_command() {
        HelpFormatter formatter = new HelpFormatter();

        final PrintWriter writer = new PrintWriter(System.out);
        formatter.printUsage(writer,80,"GGIT", options);
        writer.flush();
    }

    private static boolean checkDataStore() {
        String fileName = "repo.ser";

        File directory = new File(GGITConst.DATASTORE_PATH);
        if (!directory.exists()){
            directory.mkdir();
        }

        File repo = new File(GGITConst.DATASTORE_PATH + "/ggit_repo.ser");
        if (repo.exists() && !repo.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }

    private static void serialize() {
        GGITInfo info = new GGITInfo(repo, currentNode);
        try {
            FileOutputStream fileOut =
                    new FileOutputStream(GGITConst.DATASTORE_PATH + "/ggit_repo.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(info);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in " + GGITConst.DATASTORE_PATH + "/ggit_repo.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    private static void deserialize() {
        GGITInfo info;
        try {
            FileInputStream fileIn = new FileInputStream(GGITConst.DATASTORE_PATH + "/ggit_repo.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            info = (GGITInfo) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("GGITInfo class not found");
            c.printStackTrace();
            return;
        }
    }
}
