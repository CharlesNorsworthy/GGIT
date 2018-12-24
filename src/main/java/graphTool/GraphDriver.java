package graphTool;

import java.util.HashMap;
import java.util.Scanner;

import org.neo4j.graphdb.Label;

public class GraphDriver{

    private boolean running;

    private Scanner input;
    private DbOps dbOps;

    public GraphDriver(){
        input = new Scanner(System.in);
        String path = setPath();
        dbOps = new DbOps(path);
        running = true;
    }

    public static void main(String args[]) {
        GraphDriver graphDriver = new GraphDriver();
        graphDriver.Drive();
    }

    private String setPath() {
        String ans, path = null;

        System.out.println("Would you like to use the default database path? (YES or NO)");
        ans = input.nextLine();
        if (ans.equals("YES") || ans.equals("yes") || ans.equals("Yes")) {
            path = "\\C:\\Neo4J";
        } else if (ans.equals("NO") || ans.equals("no") || ans.equals("No")) {
            System.out.println("Enter in a path for the database: ");
            path = input.nextLine();
        } else {
            System.out.println("Enter 'YES' or 'NO'");
            setPath();
        }

        return path;
    }

    private void showMainMenu(){

        System.out.println("------------- Graph Tool Driver ------------------\n\n");
        System.out.println("\tMAIN-MENU\n");
        System.out.println("1 - Create Observation");
        System.out.println("2 - Create Knowledge");
        System.out.println("3 - Update Observation");
        System.out.println("4 - Update Knowledge");
        System.out.println("5 - Delete Observation");
        System.out.println("6 - Delete Knowledge");
        System.out.println("7 - More Commands");
        System.out.println("0 - Exit Program");
        System.out.println("----------------------------------------------------\n");
        System.out.println("Select a command to execute:");

        executeMainCommand(input.nextLine());
    }

    private void showMoreOptions(){

        System.out.println("------------- Graph Tool Driver ------------------\n\n");
        System.out.println("\tMORE-COMMANDS\n");
        System.out.println("1 - List all Observations");
        System.out.println("2 - List all Knowledges");
        System.out.println("3 - List all Relationships");
        System.out.println("4 - Create Default Nodes");
        System.out.println("5 - Delete All Nodes");
        System.out.println("0 - Main Menu");
        System.out.println("----------------------------------------------------\n");
        System.out.println("Select a command to execute:");

        executeMoreCommand(input.nextLine());
    }

    private void executeMainCommand(String args){
        try {
            String input = args;
            switch (input) {
                case "1":
                    initCreateNode(Const.OBSERVATION_LABEL);
                    break;
                case "2":
                    initCreateNode(Const.KNOWLEDGE_LABEL);
                    break;
                case "3":
                    initUpdateNode(Const.OBSERVATION_LABEL);
                    break;
                case "4":
                    initUpdateNode(Const.KNOWLEDGE_LABEL);
                    break;
                case "5":
                    initDeleteNode(Const.OBSERVATION_LABEL);
                    break;
                case "6":
                    initDeleteNode(Const.KNOWLEDGE_LABEL);
                    break;
                case "7":
                    showMoreOptions();
                    break;
                case "0":
                    exitProgram();
                    break;
                default:
                    System.out.println("The user must enter a command");
            }
        } catch (UnsupportedOperationException e) {
            System.out.println("Operation does not currently exist. We'll get to it eventually.");
        }
    }

    private void executeMoreCommand(String args){
        try {
            String input = args;
            switch (input) {
                case "1":
                    initReadAllNodes(Const.OBSERVATION_LABEL);
                    break;
                case "2":
                    initReadAllNodes(Const.KNOWLEDGE_LABEL);
                    break;
                case "3":
                    initReadAllRelationShips();
                    break;
                case "4":
                    initCreateDefaultNodes();
                    break;
                    //throw new UnsupportedOperationException("The \"Coming Soon\" command is not currently supported. We'll get to it one day");
                case "5":
                    initDeleteAllNodes();
                    break;
                case "0":
                    showMainMenu();
                    break;
                default:
                    System.out.println("The user must enter a command");
            }
        } catch (UnsupportedOperationException e) {
            System.out.println(e);
        }
    }

    private void initCreateNode(Label label){
        System.out.println("\n...Creating a new " + label.name() + ":\n");
        System.out.print("...Set the following property values: ");
        System.out.print("[ \"id\", \"name\", \"latitude\", \"longitude\", \"description\" ]\n");

        HashMap<String, Object> propertyToValues = new HashMap<>();

        for(String prop : Const.NODE_PROPERTIES)
        {
            propertyToValues = HandleValueInput(prop, propertyToValues);
        }

        if(label == Const.OBSERVATION_LABEL)
        {
            dbOps.createObservation(propertyToValues);
        }
        else if(label == Const.KNOWLEDGE_LABEL)
        {
            System.out.println("...Choose available [Observation] to link new [Knowledge] to:");

            boolean valid = false;
            String obsId = new String();

            while(!valid)
            {
                HashMap<String, HashMap<String, Object>> allNodes = dbOps.getAllObservations();
                for (String key : allNodes.keySet())
                {
                    System.out.println("--( { \"" + Const.UUID + "\" : " + key + "} )--");
                }
                System.out.println("------------------------------------------------------");
                System.out.println("Enter id of [Observation]:");

                obsId = input.nextLine();
                if(allNodes.keySet().contains(obsId))
                {
                    valid = true;
                }
                else
                {
                    System.out.println("Invalid [Observation] id.\n\n\n\n");
                    input.nextLine();
                }
            }
            dbOps.createKnowledge(obsId, propertyToValues);
        }
        else
        {
            System.out.println("Invalid Label.\n\n\n\n");
            input.nextLine();
        }
    }

    private void initUpdateNode(Label label){
        System.out.println("\n...Update an existing " + label.name() + ":\n");

        HashMap<String, Object> propertyToValues = new HashMap<>();
        HashMap<String, Object> readNode;
        boolean valid = false;

        while(!valid) {
            System.out.print("...Enter the Id value for the node being updated: ");
            String idValue = input.nextLine();
            if (label == Const.OBSERVATION_LABEL) {
                readNode = dbOps.getObservation(idValue);
                if(readNode != null) {
                    propertyToValues = EditNodeProperties(readNode);
                    dbOps.updateObservation(idValue, propertyToValues);
                    valid = true;
                }
                else
                {
                    System.out.println("Invalid [Observation] id.");
                    input.nextLine();
                }
            } else if (label == Const.KNOWLEDGE_LABEL) {

                readNode = dbOps.getKnowledge(idValue);
                if(readNode != null){
                    propertyToValues = EditNodeProperties(readNode);
                    dbOps.updateKnowledge(idValue, propertyToValues);
                    valid = true;
                }
                else{
                    System.out.println("Invalid [Knowledge] id.");
                    input.nextLine();
                }
            } else {
                System.out.println("Invalid Label.\n\n\n\n");
                input.nextLine();
                valid = true;
            }
        }
    }

    private void initDeleteNode(Label label){
        System.out.println("\n...Update an existing " + label.name() + ":\n");
        System.out.print("...Enter the Id value for the node being updated: ");

        String idValue = input.nextLine();

        if(label == Const.OBSERVATION_LABEL)
        {
            dbOps.deleteObservation(idValue);
        }
        else if(label == Const.KNOWLEDGE_LABEL)
        {
            dbOps.deleteKnowledge(idValue);
        }
        else
        {
            System.out.println("Invalid Label.\n\n\n\n");
            input.next();
        }
    }

    private void initReadAllNodes(Label label){
        HashMap<String, HashMap<String, Object>> nodes;
        if(label == Const.OBSERVATION_LABEL)
        {
            nodes = dbOps.getAllObservations();
            System.out.println("OBSERVATIONS-----");
            for(String nodeId : nodes.keySet()){
                System.out.println("[{ id: \"" + nodeId + "\" , name: \"" + nodes.get(nodeId).get(Const.NAME) + "\"}] -");
            }
        }
        else if(label == Const.KNOWLEDGE_LABEL)
        {
            nodes = dbOps.getAllKnowledges();
            System.out.println("KNOWLEDGES-----");
            for(String nodeId : nodes.keySet()){
                System.out.println("[{ id: \"" + nodeId + "\" , name: \"" + nodes.get(nodeId).get(Const.NAME) + "\"}] -");
            }
        }
        else {
            System.out.println("Invalid label ....\n");
            input.nextLine();
        }
    }

    private void initReadAllRelationShips() {
        System.out.println("Showing all relationships...");
        if(dbOps.getAllRelationShips().isEmpty()){
            System.out.println("No relationships found.");
        }
    }

    private void exitProgram(){
        running = false;
    }

    public void Drive(){
        while(running){
            showMainMenu();
        }
    }

    boolean TryParseDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private HashMap<String, Object> EditNodeProperties(HashMap<String,Object> readNode){
        HashMap<String, Object>propertyToValues = readNode;
        System.out.println("\nRetrieved node with properties: [ \"id\", \"name\", \"latitude\", \"longitude\", \"description\" ]");
        String chooseProp = new String();

        while (!chooseProp.equals("done"))
        {
            for (String prop : propertyToValues.keySet())
            {
                System.out.println("[\"" + prop + "\"] <- {" + propertyToValues.get(prop) + "}");
            }
            System.out.println("--------------------------------------------------------");
            System.out.print("Enter property to edit: (Type DONE to when finished)");

            boolean valid = false;
            while (!valid)
            {
                chooseProp = input.nextLine();

                switch (chooseProp.toLowerCase())
                {
                    case Const.UUID:
                        System.out.println("Cannot change Node id.\n");
                        valid = true;
                        break;
                    case Const.NAME:
                        propertyToValues = HandleValueInput(Const.NAME, propertyToValues);
                        valid = true;
                        break;
                    case Const.LATITUDE:
                        propertyToValues = HandleValueInput(Const.LATITUDE, propertyToValues);
                        valid = true;
                        break;
                    case Const.LONGITUDE:
                        propertyToValues = HandleValueInput(Const.LONGITUDE, propertyToValues);
                        valid = true;
                        break;
                    case Const.DESCRIPTION:
                        propertyToValues = HandleValueInput(Const.DESCRIPTION, propertyToValues);
                        valid = true;
                        break;
                    case "done":
                        System.out.print("...Finishing property edit.\n\n");
                        valid = true;
                        chooseProp = "done";
                        break;
                    default:
                        System.out.println("Invalid property. Choose a valid node property:");
                }
            }
        }

        return propertyToValues;
    }

    private HashMap<String, Object> HandleValueInput(String prop, HashMap<String,Object> propertyToValues){
        System.out.println("Enter a new value for \"" + prop +"\":");
        System.out.print("[\"" + prop +"\"] -> ");
        String value = input.nextLine();

//        if(TryParseDouble(value))
//        {
//            Double dValue = Double.parseDouble(value);
//            propertyToValues.put(prop, dValue);
//        }
//        else
//        {
//            propertyToValues.put(prop, value);
//        }
        propertyToValues.put(prop, value);
        return propertyToValues;
    }

    private void initCreateDefaultNodes(){
        dbOps.createDefaultNodes();
    }

    private void initDeleteAllNodes(){
        dbOps.deleteAllNodes();
    }
}

