package GGIT;

import org.apache.commons.cli.Options;

/**
 * GGIT.GGIT.GGITConst holds variables used throughout GGIT.GGIT/src/main/java
 */
public class GGITConst {
    public static final String DATASTORE_PATH = "C:/Users/kchampagne/workspace/GGIT/datastore";

    public static final String MASTER = "master";

    public static final Options OPTIONS = new Options()
            .addOption("init", true, "Create an empty GGIT.GGIT repository")
            .addOption("clone", true, "Clone a repository into a new directory")
            .addOption("add", true, "Add file contents to the index")
            .addOption("commit", true, "Record changes to the repository")
            .addOption("push", true, "Update remote refs along with associated objects")
            .addOption("status", true, "Show the working tree status")
            .addOption("remote", true, "Manage set of tracked repositories")
            .addOption("checkout", true, "Switch branches or restore working tree files")
            .addOption("branch", true, "List, create, or delete branches")
            .addOption("pull", true, "Fetch from and integrate with another repository or a local branch")
            .addOption("merge", true, "Join two or more development histories together")
            .addOption("diff", true, "Show changes between commits, commit and working tree, etc")
            .addOption("log", false, "Show commit logs")
            .addOption("fetch", true, "Download objects and refs from another repository")
            .addOption("reset", true, "Reset current HEAD to the specified state")
            .addOption( "grep", true, "Print lines matching a pattern");
}
