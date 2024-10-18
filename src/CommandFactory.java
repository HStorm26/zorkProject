
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
        
        // For now, only one type of command object, to move and to save.
        if (commands[0].equals("n") || commands[0].equals("s") ||
            commands[0].equals("e") || commands[0].equals("w") ||
            commands[0].equals("u") || commands[0].equals("d") ){
            return new MovementCommand(command);
        }
        else 
            if (commands[0].equals("save")) {
            return new SaveCommand(commands[1]);
        }
        else {
            return new UnknownCommand(command);
        }
    }

}
