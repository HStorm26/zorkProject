
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
        //this should probably be a switch case
        if (commands[0].equals("n") || commands[0].equals("s") ||
            commands[0].equals("e") || commands[0].equals("w") ||
            commands[0].equals("u") || commands[0].equals("d") ){
            return new MovementCommand(command);
        }
        //save needs to check for an argument
        else if (commands[0].equals("save")) {
            return new SaveCommand(checkForArgs(commands));
        }
        //default case
        else {
            return new UnknownCommand(commands[0]);
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
