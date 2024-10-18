//Command for saving files
import java.util.Scanner;
class SaveCommand extends Command {
    private String saveFilename;
    
    SaveCommand(String command) {
        this.saveFilename = (command);
    }
    String execute() {
        if(saveFilename.equals("")){
            return "Please enter a save name.\n";
        }
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
