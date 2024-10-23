import java.io.IOException;

class SaveCommand extends Command {
    private String saveFilename;

    public SaveCommand(String saveFilename) {
        this.saveFilename = saveFilename;
    }

    @Override
    public String execute() {
        if (saveFilename.isEmpty()) {
            return "Please enter a save name.\n";
        }

        try {
            GameState.instance().store(saveFilename);
            return "Data saved to " + saveFilename + ".\n";
        } catch (IOException e) {
            return "Save failed.\n";
        }
    }
}
