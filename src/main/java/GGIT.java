import graphTool.GraphDriver;

/** GGIT is the driver class for this Graph Database Version Control System
 * Command line input drives this class
 */
public class GGIT
{
    public static void main(String args[]) {
        GraphDriver graphDriver = new GraphDriver();
        graphDriver.Drive();
        //executeCmd(args);
    }

<<<<<<< HEAD
import java.util.Scanner;

    public class GGIT
    {


        public static void main(String args[]) {

            menu();

            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();

            executeCmd(input);
        }

        private static void executeCmd(String input){
            graphTool.GraphApi database;
            database = new graphTool.GraphApi();
            try {
                Scanner sc = new Scanner(System.in);
                menu();
                switch (input) {
                    case "init":

////
//                        database.createRelationship("Observation","thermal","Knowledge","sonarData");
//                        database.createRelationship("Root","root","Observation","thermal");
//                        database.createObservation("thermal");
//                        database.createObservation("flight");
//                        database.showObservationByType("thermal");
//                        database.showObservationByType("flight");
//                        database.showRoot("root");
//

                    case "clone":

                        throw new UnsupportedOperationException("The " + input + " command is not currently supported.");

                    case "add":

                        throw new UnsupportedOperationException("The " + input + " command is not currently supported.");
                    case "commit":
                        throw new UnsupportedOperationException("The " + input + " command is not currently supported.");
                    case "push":
                        throw new UnsupportedOperationException("The " + input + " command is not currently supported.");
                    case "status":
                        System.out.println("Please enter node,label,and property you want to select separated by commas");
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
            } catch (UnsupportedOperationException exception) {
                System.out.println(exception);
=======
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
>>>>>>> 12f1d15a1b2be46d153f0793ee21072665952df6
            }
        } catch (UnsupportedOperationException e) {
            System.out.println(e);
        }
    }
}
