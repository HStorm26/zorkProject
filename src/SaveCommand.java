import java.io.IOException;
import java.util.Scanner;
class SaveCommand extends Command {
    private String saveFilename;

    public SaveCommand(String saveFilename) {
        this.saveFilename = saveFilename;
    }

    @Override
    public String execute() {
        if (saveFilename.isEmpty()) {
            System.out.println("Please enter a save name.\n");
            Scanner stdin = new Scanner(System.in);
           String savfile = stdin.nextLine();
            if(!savfile.endsWith(".sav")){
                savfile += ".sav";
            }
            this.saveFilename=savfile;
           // return "Please enter a save name.\n";
     
       
        }

        try {
            GameState.instance().store(saveFilename);
            return "Data saved to " + saveFilename + "\n";
        } catch (IOException e) {
            return "Save failed.\n";
        
        }
    }
}
