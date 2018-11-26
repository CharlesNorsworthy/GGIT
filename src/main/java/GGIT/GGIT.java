package GGIT;

import java.io.*;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;

/** GGIT.GGIT is the driver class for this Graph Database Version Control System
 * Command line input drives this class
 */
public class GGIT {
    private static GGITGraph repo;

    private static GGITNode currentNode;

    private static Options options;

    public static void main(String args[]) {
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
//            serialize();
        }
    }

    private static void _init(String[] args) {
        if (repo == null) {
            if (args.length > 1) {
                String graphRef = args[1];
                GGITGraph repo = new GGITGraph(graphRef);
                if (repo != null) {
                    System.out.println("A repo was successfully initialized!");
                } else {
                    System.out.println("FAILED to initialize a repo.");
                }
            } else {
                throw new IllegalArgumentException("You must specify a location to initialize the repository!");
            }
        } else {
            throw new IllegalArgumentException("There is already a repository that exists.");
        }
    }

    private static void _clone(String[] args) {
        if (args.length > 1) {
            File repoRef = new File(args[1]);
            if (repoRef.isDirectory()) {
                File copyToRef;
                if (args.length > 2) {
                    copyToRef = new File(args[2]);
                } else {
                    copyToRef = new File(System.getProperty("user.dir"));
                }
                if (!copyToRef.exists()) {
                    copyToRef.mkdir();
                }

                try {
                    FileUtils.copyDirectory(repoRef, copyToRef);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            throw new IllegalArgumentException("A repository must be specified to clone.");
        }
    }

    private static void _commit(String[] args) {
        File zipDir;
        if (args.length > 1) {
            zipDir = new File(args[1]);
        } else {
            zipDir = new File(System.getProperty("user.dir"));
        }
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
        repo.listBranches();
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
}
