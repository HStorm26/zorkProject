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
        // Trim the command to remove any trailing or leading spaces or characters
        command = command.trim();

        String[] commands = command.split(" ");
        String c = commands[0];

        switch (c) {
            case "n":
            case "s":
            case "e":
            case "w":
            case "u":
            case "d":
                return new MovementCommand(c);

            case "save":
                return new SaveCommand(checkForArgs(commands));

            case "take":
                return new TakeCommand(checkForArgs(commands));

            case "drop":
                return new DropCommand(checkForArgs(commands));

            case "look":
                return new LookCommand();

            case "i":
            case "inventory":
                return new InventoryCommand();

    
            default:
                if(!checkForArgs(commands).equals("")){

                return new ItemSpecificCommand(c,checkForArgs(commands));
                
                }
                else{return new UnknownCommand(c);}
      
    }
    }

    private static String checkForArgs(String[] commands) {
        if (commands.length < 2) {
            return "";
        } else {
            return commands[1].trim(); // Trim the argument to remove spaces
        }
    }
}
