
public class CommandFactory{

    private static CommandFactory theInstance = null;

    public static synchronized CommandFactory instance() {
        if (theInstance == null) {
            theInstance = new CommandFactory();
        }
        return theInstance;
    }

    private CommandFactory() {
    }

    public Command parse(String command) {
        String[] commands = command.split(" ");
        String c = commands[0];
        switch(c) {
            case "n":
            case "s":
            case "e":
            case "w":
            case "u":
            case "d":
                return new MovementCommand(c);
        //save needs to check for an argument
            case "save":
                return new SaveCommand(checkForArgs(commands));
//don't uncomment this code until Look and Inventory are probably implemented. 
/*
        //look does not need any arguments
            case "look":
                return new LookCommand();
            case "i":
                return new InventoryCommand();
*/
        //default case
            default:
                return new UnknownCommand(c);
        }
    }
    //checks if a command that needs an argument has an argument passed to it.
    //each command subclass that needs an argument will have a case for "" being passed.
    private static String checkForArgs(String[] commands){
        if(commands.length < 2){
            return "";
        }
        else{
            return commands[1];
        }
    }
}
