/** GGIT is the driver class for this Graph Database Version Control System
 * Command line input drives this class
 */
public class GGIT
{
    public static void main(String args[]) {
        executeCmd(args);
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
