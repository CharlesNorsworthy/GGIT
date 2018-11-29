package GGIT;

import java.io.*;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import VersionControl.Merge;
import graphTool.DbUtils;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;
import org.neo4j.graphdb.Label;

/** GGIT.GGIT is the driver class for this Graph Database Version Control System
 * Command line input drives this class
 */
public class GGIT {
    //Current branch of the repository
    private static String currentBranch;

    //UUID of the node representing the node referring to the most recent update of the graph database.
    private static String currentNode;

    //Reference path to the local repository
    private static String localRepoPath;

    //Reference path to the remote repository
    private static String remoteRepoPath;

    private static GGITGraph repo;

    private static Options options;

    private static class GGITConfigValues {
        String result = "";
        InputStream inputStream;

        private String getPropValues(String propFileName) throws IOException {
            try {
                Properties prop = new Properties();
                inputStream = new FileInputStream(propFileName);
                prop.load(inputStream);

                // get the property value and print it out
                remoteRepoPath = prop.getProperty("remoteRepoPath");
                localRepoPath = prop.getProperty("localRepoPath");
                currentNode = prop.getProperty("currentNode");
                currentBranch = prop.getProperty("currentBranch");
            } catch (FileNotFoundException e){
                System.out.println("FileNotFoundException :: property file '" + propFileName + "' not found in the classpath");
            }
            catch (Exception e) {
                System.out.println("Exception: " + e);
            } finally {
                inputStream.close();
            }
            return result;
        }

        private void setPropValues(String propFileName) {
            try {
                Properties properties = new Properties();
                properties.setProperty("remoteRepoPath", (remoteRepoPath == null ? "null" : remoteRepoPath));
                properties.setProperty("localRepoPath", (localRepoPath == null ? "null" : localRepoPath));
                properties.setProperty("currentNode", (currentNode == null ? "null" : currentNode));
                properties.setProperty("currentBranch", (currentBranch == null ? "null" : currentBranch));


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
                        _branch();
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
                    case "help":
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

    /**
     * Creates a new remote repository
     * @param args
     */
    private static void _init(String[] args) {
        if (repo == null) {
            String graphRef;
            Scanner input = new Scanner(System.in);

            System.out.println("Enter a location for the new repository to exist (y for this directory):");
            remoteRepoPath = input.nextLine();
            if (remoteRepoPath.equals("y") || remoteRepoPath.equals("Y")) {
                remoteRepoPath = "\\C:\\Users\\the88\\Desktop\\CMPS411\\GGIT\\repositories\\remote";//System.getProperty("user.dir") + "\\repository";
                System.out.println(remoteRepoPath);
            }
            File remote = new File(remoteRepoPath);
            if (!remote.exists()) {
                remote.mkdir();
            }

            if (args.length > 1) {
                graphRef = args[1];
                try {
                    repo = new GGITGraph(remoteRepoPath);
                    currentNode = repo.initRepo(graphRef);
                    currentBranch = GGITConst.MASTER;
                    System.out.println("A repo was successfully initialized!");
                } catch(Exception e) {
                    System.out.println("FAILED to initialize a repo. " + e.toString());
                }

            } else {
                throw new IllegalArgumentException("You must specify a graph reference to initialize in the repository!");
            }
        } else {
            throw new IllegalArgumentException("There is already a repository that exists.");
        }
    }

    /**
     * Copies the "remote" repository to the given path for the "local" repository
     * @param args
     */
    private static void _clone(String[] args) {
        if (remoteRepoPath != null) {
            File repoRef = new File(remoteRepoPath);
            if (repoRef.isDirectory()) {
                if (args.length > 1) {
                    localRepoPath = args[1];
                } else {
                    localRepoPath = System.getProperty("user.dir");
                }
                File copyToRef = new File(localRepoPath);
                if (!copyToRef.exists()) {
                    copyToRef.mkdir();
                }

                try {
                    repo.closeGraph();
                    FileUtils.copyDirectory(repoRef, copyToRef);
                    if (copyToRef.isDirectory()) {
                        System.out.println("Remote '" + remoteRepoPath + "' cloned to local '" + localRepoPath + "'");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            throw new IllegalArgumentException("A repository must be specified to clone.");
        }
    }

    /**
     * Makes a snapshot of the graph database directory for the currentNode's graph reference and zips the folder with the currentNode UUID as the name.
     * That folder is saved under the '_versions' sub directory in the local repository. Can save a message on commit.
     * @param args
     */
    private static void _commit(String[] args) {
        String versionsPath = Paths.get(System.getProperty("user.dir"), "\\repositories\\local\\_versions").toString();
        File versionsDir = new File(versionsPath);
        HashMap<String, Object> currentGraph = repo.readNode(currentNode, currentBranch);

        String message = "";
        if(args.length > 1){
            message = args[1];
        }

        if (!versionsDir.exists()){
            versionsDir.mkdir();
        }

        if (versionsDir.isDirectory()){
            createDbSnapshot(currentGraph);

            if (new File(Paths.get(versionsPath, currentNode).toString()).exists()){
                String graphRef = currentGraph.get(GGITConst.GRAPH_REFERENCE).toString();
                String branch = currentGraph.get(GGITConst.BRANCH).toString();
                String previousNode = repo.getCurrNode(branch);
                if (args.length > 2) {
                    graphRef = args[2];
                    if (args.length > 3) {
                        branch = args[3];
                    }
                }
                currentNode = repo.addNode(graphRef, branch, message, previousNode);

                System.out.println("[ " + branch + "] " + currentNode + "  commit: " + message);
            }
        } else {
            System.out.println("Cannot locate '_versions' directory  in the local repository path : " + localRepoPath);
        }
    }

    /**
     * Integrate the local repository into the remote repository
     * @param args
     */
    private static void _push(String[] args) {
        GGITGraph remote = new GGITGraph(remoteRepoPath);
        if (remote == null) {
            throw new IllegalArgumentException("There is no remote repository.");
        }
        //Naive merge
    }

    /**
     * Displays the properties of the "current" node in the repository
     * @param args
     */
    private static void _status(String[] args) {
        if (repo != null) {
            HashMap<String, Object> props = repo.readNode(currentNode, currentBranch);
            System.out.println("Displaying current node of repository:");
            for (String key: props.keySet()){
                String value;
                if (key.equals(GGITConst.TIMESTAMP)) {
                    Date date = new Date();
                    date.setTime((long) props.get(key));
                    value = new SimpleDateFormat("MM/dd/yy HH:mm:ss").format(date);
                } else {
                    value = props.get(key).toString();
                }
                System.out.println(key + ": " + value);
            }
        }
    }

    /**
     * Changes the remote repository to the given reference
     * @param args
     */
    private static void _remote(String[] args) {
        if (repo != null) {
            if (args.length > 1) {
                remoteRepoPath = args[1];
            }
        }
    }

    /**
     * Switches the "current" node of the repository to the given branch
     * @param args
     */
    private static void _checkout(String[] args) {
        if (repo != null) {
            if (args.length > 1) {
                //branch
                String branch = args[1];
                if (repo.doesBranchExist(branch)) {
                    currentBranch = branch;
                    currentNode = repo.getCurrNode(branch);
                } else {
                    String[] params  = new String[4];
                    //command
                    params[0] = args[0];
                    params[3] = branch;
                    if (args.length > 2) {
                        //message
                        params[1] = args[2];
                        if (args.length > 3) {
                            //graphRef
                            params[2] = args[3];
                        }
                    }
                    _commit(params);
                    currentBranch = branch;
                    System.out.println("Created branch '" + currentBranch + "'");
                }
            }
        }
    }

    /**
     * Lists available branches
     */
    private static void _branch() {
        if (repo == null) {
            throw new IllegalArgumentException("A repository must be initialized to be branched.");
        }
        repo.getLabels().forEach(label -> System.out.println(label.name()));
    }

    /**
     * Integrate the remote repository into the local repository
     * @param args
     */
    private static void _pull(String[] args) {
        GGITGraph remote = new GGITGraph(remoteRepoPath);
        if (remote == null) {
            throw new IllegalArgumentException("There is no remote repository.");
        }
        if (repo != null) {

        }
    }

    /**
     * Integrates one local branch into another local branch
     * @param args
     */
    private static void _merge(String[] args) {
        if (args.length > 1) {
            String branch1 = args[1];
            String branch2;
            if (args.length > 2) {
                branch2 = args[2];
            } else {
                branch2 = GGITConst.MASTER;
            }
            String message = "Merged " + branch1 + " with "+ branch2;
            String branch1Node = repo.getCurrNode(branch1); String branch2Node = repo.getCurrNode(branch2);
            String preMergeB1 = createDbSnapshot(repo.readNode(branch1Node, branch1)); String preMergeB2 = createDbSnapshot(repo.readNode(branch2Node, branch2));
            _checkout(args);

            GGITGraph graph1 = new GGITGraph(preMergeB1);
            GGITGraph graph2 = new GGITGraph(preMergeB2);
            Merge.mergeNaively(graph1.db, graph2.db, repo.db);
            graph1.closeGraph(); graph2.closeGraph();
            _commit(new String[]{ message });
        } else {
            System.out.println("You must enter two branch names!");
        }
    }

    /**
     * Gets the list of branches on the "remote" repository
     * @param args
     */
    private static void _fetch(String[] args) {
        try {
            GGITGraph remote = new GGITGraph(remoteRepoPath);
            remote.getLabels().forEach(label -> System.out.println(label.name()));
        } catch (Exception e){
            throw new IllegalArgumentException("There is no remote repository.");

        }
    }

    /**
     * Dials back the repository on the current branch
     * @param args
     */
    private static void _reset(String[] args) {
        Label label = Label.label(currentBranch);
        if(repo.isStartOfBranch(label, currentNode) && !currentBranch.equals(GGITConst.MASTER)){
            if(repo.getCurrNode(currentBranch) != currentNode ) {
                repo.removeNode(label, currentNode);
                currentNode = repo.getCurrNode(currentBranch);
            }
        } else if(repo.isStartOfBranch(label, currentNode)){
            //Are you sure you want to get rid of root for Master?
        }
    }

    private static void _no_command() {
        HelpFormatter formatter = new HelpFormatter();

        final PrintWriter writer = new PrintWriter(System.out);
        formatter.printUsage(writer,80,"GGIT", options);
        writer.flush();
    }

    private static boolean bootUp(String args[]) {
        String path = Paths.get(System.getProperty("user.dir"), "config.properties").toString();

        if ((new File(path)).exists()) {
            GGITConfigValues config = new GGITConfigValues();
            try {
                config.getPropValues(path);
                if (!remoteRepoPath.equals("null")) {
                    repo = new GGITGraph((localRepoPath == null ? remoteRepoPath : localRepoPath));
                }
            } catch (IOException e) {
                System.out.println("IOException :: " + e);
            }
            return true;
        } else {
            if (args.length > 0) {
                if (args[0].equals("init") || args[0].equals("clone") || args[0].equals("help") || args[0].equals("")) {
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
        String path = Paths.get(System.getProperty("user.dir"), "config.properties").toString();

        GGITConfigValues config = new GGITConfigValues();
        try {
            config.setPropValues(path);
        } catch (Exception e) {
            System.out.println("IOException :: " + e);
        }
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    private static String createDbSnapshot(HashMap<String, Object> currentGraph){
        File dbSnapshot = new File(currentGraph.get(GGITConst.GRAPH_REFERENCE).toString());
        if (dbSnapshot.isDirectory()) {
            try {
                repo.closeGraph();
                FileOutputStream fos = new FileOutputStream(currentNode + ".zip");
                ZipOutputStream zipOut = new ZipOutputStream(fos);
                zipFile(dbSnapshot, currentNode + ".zip", zipOut);
                zipOut.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return currentNode + ".zip";
    }
}
