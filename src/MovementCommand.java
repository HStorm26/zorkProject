public class MovementCommand extends Command {
    private String direction;

    public MovementCommand(String direction) {
        this.direction = direction;
    }

    @Override
    public String execute() {
        Room currentRoom = GameState.instance().getAdventurersCurrentRoom();
        Room nextRoom = currentRoom.leaveBy(this.direction);
        if (nextRoom != null) {
            GameState.instance().setAdventurersCurrentRoom(nextRoom);
            return nextRoom.describeOnEntry();  // Use describeOnEntry() here
        } else {
            return "Sorry, you can't go " + this.direction + " from " + currentRoom.getName() + ".\n";
        }
    }
}
