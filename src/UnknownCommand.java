//for when the user writes some absolute nonsense
class UnknownCommand extends Command {
    private String bogus;
    
    UnknownCommand(String command){
        this.bogus = command;
    }
    String execute() {
        return "I'm sorry, I don't understand the command '" + bogus + "'.\n";
    }
}
