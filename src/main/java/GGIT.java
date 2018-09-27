
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

//
                        database.createRelationship("Observation","thermal","Knowledge","sonarData");
                        database.createRelationship("Root","root","Observation","thermal");
                        database.createObservation("thermal");
                        database.createObservation("flight");
                        database.showObservationByType("thermal");
                        database.showObservationByType("flight");
                        database.showRoot("root");


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
            }

        }

        public static void menu () {
            System.out.println("Please select an option");
            System.out.println("1: Initilize Database");
            System.out.println("2: clone");
            System.out.println("3: add");
            System.out.println("4: commit");
//            System.out.println("5: push");
//            System.out.println("6: pull");
//            System.out.println("7: fetch");
//            System.out.println("8: merge");
//            System.out.println("9: status");
//            System.out.println("10: remote");
//            System.out.println("11: checkout");
        }
    }


