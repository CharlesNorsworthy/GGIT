package graphTool;

public class GraphDriver{

    public GraphDriver(){

    }

    private void showMainMenu(){

        System.out.println("------------- Graph Tool Driver ------------------\n\n");
        System.out.println("\tMAIN-MENU\t");
        System.out.println("Select a command to execute:");
        System.out.println("1 - Create Observation");
        System.out.println("2 - Create Knowledge");
        System.out.println("3 - Create Relationship");
        System.out.println("4 - Update Observation");
        System.out.println("5 - Update Knowledge");
        System.out.println("6 - Update Relationship");
        System.out.println("7 - Delete Observation");
        System.out.println("8 - Delete Knowledge");
        System.out.println("9 - Delete Relationship");
        System.out.println("0 - More Commands");
        System.out.println("----------------------------------------------------\n\n");
    }

    private void showMoreOptions(){

        System.out.println("------------- Graph Tool Driver ------------------\n\n");
        System.out.println("Select a command to execute:");
        System.out.println("1 - Coming Soon");
        System.out.println("2 - Create Knowledge");
        System.out.println("3 - Create Relationship");
        System.out.println("0 - Main Menu");
        System.out.println("----------------------------------------------------\n\n");
    }
}