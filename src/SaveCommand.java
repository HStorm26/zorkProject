//Command for saving files
import java.util.Scanner;
class SaveCommand extends Command {
    private String saveFilename;
    
    SaveCommand(String command) {
        this.saveFilename = (command);
    }
    String execute() {
        try {
            GameState.instance().store(saveFilename);
            return "Data saved to " +
                GameState.instance().getFullSaveName(saveFilename) + ".\n";
        } catch (Exception e) {
            System.err.println("Couldn't save!");
            e.printStackTrace();
            return "";
        }
    }
}
