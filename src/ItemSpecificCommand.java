public class ItemSpecificCommand extends Command {
    private String verb;
    private String noun;

    public ItemSpecificCommand(String verb, String noun) {
        this.verb = verb;
        this.noun = noun;
    }

    @Override
    public String execute() {
        Room currentRoom = GameState.instance().getAdventurersCurrentRoom();
        Item item = currentRoom.getItemNamed(noun);

        if (item == null) {
            return "There is no " + noun + " here to " + verb + ".\n";
        }

        String actionResult = item.getMessageForVerb(verb);
        return actionResult + "\n";
    }
}
