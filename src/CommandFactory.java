
public class CommandFactory {

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
        // For now, only one type of command object, to move and to save.
        return new Command(command);
    }

}
