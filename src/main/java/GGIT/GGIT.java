package GGIT;

import java.io.*;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;

/** GGIT.GGIT is the driver class for this Graph Database Version Control System
 * Command line input drives this class
 */
public class GGIT {
    //UUID of the node representing the node referring to the most recent update of the graph database.
    private static String currentNode;

    //Reference path to the repository
    private static String repoPath;

    private static GGITGraph repo;

    private static Options options;

    private static class GGITConfigValues {
        String result = "";
        InputStream inputStream;

        private String getPropValues(String propFileName) throws IOException {
            try {
                Properties prop = new Properties();

                inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

                if (inputStream != null) {
                    prop.load(inputStream);
                } else {
                    throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
                }

                // get the property value and print it out
                repoPath = prop.getProperty("repoPath");
                currentNode = prop.getProperty("currentNode");
            } catch (Exception e) {
                System.out.println("Exception: " + e);
            } finally {
                inputStream.close();
            }
            return result;
        }

        private void setPropValues(String propFileName) {
            try {
                Properties properties = new Properties();
                properties.setProperty("repoPath", repoPath);
                properties.setProperty("currentNode", currentNode);

                File file = new File(propFileName);
                FileOutputStream fileOut = new FileOutputStream(file);
                properties.store(fileOut, null);
                fileOut.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) {
        if (bootUp(args)) {
            options = GGITConst.OPTIONS;
            executeCmd(args);
        }
        writeConfig();
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
            System.out.println("IllegalArgumentException :: " + e);
        }
    }

    private static void _init(String[] args) {
        if (repo == null) {
            String graphRef;
            Scanner input = new Scanner(System.in);

            System.out.println("Enter a location for the new repository to exist (y for this directory):");
            repoPath = input.nextLine();
            if (repoPath.equals("y") || repoPath.equals("Y")) {
                repoPath = Paths.get(System.getProperty("user.dir"), "/graph.db").toString();
            }
            if (args.length > 1) {
                graphRef = args[1];
                try {
                    repo = new GGITGraph(repoPath);
                    currentNode = repo.initRepo(graphRef);
                    System.out.println("A repo was successfully initialized!");
                } catch(Exception e) {
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
        if (repo != null) {
            HashMap<String, Object> props = repo.readNode(currentNode);
            System.out.println("Displaying current node of repository:");
            for (String key: props.keySet()){
                String value = props.get(key).toString();
                System.out.println(key + ": " + value);
            }
        }
    }

    private static void _remote(String[] args) {
        throw new UnsupportedOperationException("The " + args[0] + " command is not currently supported.");
    }

    private static void _checkout(String[] args) {
        throw new UnsupportedOperationException("The " + args[0] + " command is not currently supported.");
    }

    private static void _branch(String[] args) {
        if(repo == null) {
            String graphRef = "C://Neo4J";//"C:\\Users\\ncunningham\\Desktop\\TestRepo";
            repo = new GGITGraph(graphRef);
            //throw new IllegalArgumentException("A repository must be initialized to be branched.");
        }
        //else {
            if(args.length > 1){
                String branchName = args[1];

            }
            else {
                repo.listBranches();
            }
        //}
    }

    private static void _pull(String[] args) {
        throw new UnsupportedOperationException("The " + args[0] + " command is not currently supported.");
    }

    private static void _merge(String[] args) {
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

    private static boolean bootUp(String args[]) {
        String path = System.getProperty("user.dir") + "config.properties";

        if ((new File(path)).exists()) {
            GGITConfigValues config = new GGITConfigValues();
            try {
                config.getPropValues(path);
                repo = new GGITGraph(repoPath);
            } catch (IOException e) {
                System.out.println("IOException :: " + e);
            }
            return true;
        } else {
            if (args.length > 1) {
                if (args[1].equals("init") || args[1].equals("clone")) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    private static void writeConfig() {
        String path = System.getProperty("user.dir") + "config.properties";

        GGITConfigValues config = new GGITConfigValues();
        try {
            config.setPropValues(path);
        } catch (Exception e) {
            System.out.println("IOException :: " + e);
        }
    }
}
