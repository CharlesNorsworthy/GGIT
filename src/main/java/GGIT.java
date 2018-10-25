import graphTool.Const;
import graphTool.DbUtils;
import graphTool.GraphDriver;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.graphdb.Node;

import java.util.HashMap;
import java.util.List;

/** GGIT is the driver class for this Graph Database Version Control System
 * Command line input drives this class
 */
public class GGIT
{
    public static void main(String args[]) {
        GraphDriver graphDriver = new GraphDriver();
        graphDriver.Drive();
        //DbUtils db = new DbUtils("\\C:\\Neo4J");
        //db.initRoot();
        //db.createDefaultNodes();
        //db.deleteNodesByType(Const.OBSERVATION_LABEL);
        //db.deleteNodesByType(Const.KNOWLEDGE_LABEL);
        //db.deleteNodesByType(Const.ROOT_LABEL);
        //db.getNodesByType(Const.OBSERVATION_LABEL);
        //Node node = db.getNodeById(Const.OBSERVATION_LABEL, "bdfb46a4-6760-46da-8e3e-a92741466036"); //bdfb46a4-6760-46da-8e3e-a92741466036
        //System.out.println(node.toString());

    }

    private static void executeCmd(String args[]){
        try {
            String input = args[0];
            switch (input) {
                case "init":
                    throw new UnsupportedOperationException("The " + input + " command is not currently supported.");
                case "clone":
                    throw new UnsupportedOperationException("The " + input + " command is not currently supported.");
                case "add":
                    throw new UnsupportedOperationException("The " + input + " command is not currently supported.");
                case "commit":
                    throw new UnsupportedOperationException("The " + input + " command is not currently supported.");
                case "push":
                    throw new UnsupportedOperationException("The " + input + " command is not currently supported.");
                case "status":
                    throw new UnsupportedOperationException("The " + input + " command is not currently supported.");
                case "remote":
                    throw new UnsupportedOperationException("The " + input + " command is not currently supported.");
                case "checkout":
                    throw new UnsupportedOperationException("The " + input + " command is not currently supported.");
                case "branch":
                    throw new UnsupportedOperationException("The " + input + " command is not currently supported.");
                case "pull":
                    throw new UnsupportedOperationException("The " + input + " command is not currently supported.");
                case "merge":
                    throw new UnsupportedOperationException("The " + input + " command is not currently supported.");
                case "diff":
                    throw new UnsupportedOperationException("The " + input + " command is not currently supported.");
                case "log":
                    throw new UnsupportedOperationException("The " + input + " command is not currently supported.");
                case "fetch":
                    throw new UnsupportedOperationException("The " + input + " command is not currently supported.");
                case "reset":
                    throw new UnsupportedOperationException("The " + input + " command is not currently supported.");
                case "grep":
                    throw new UnsupportedOperationException("The " + input + " command is not currently supported.");
                default:
                    System.out.println("The user must enter a command");
            }
        } catch (UnsupportedOperationException e) {
            System.out.println(e);
        }
    }
}
