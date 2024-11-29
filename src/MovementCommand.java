public class MovementCommand extends Command {
    private String direction;

    public MovementCommand(String direction) {
        this.direction = direction;
    }

    @Override
    public String execute() {
        Room currentRoom = GameState.instance().getAdventurersCurrentRoom();
        Room nextRoom = currentRoom.leaveBy(this.direction);
        if (GameState.instance().combatUpdate()){
            if(nextRoom == GameState.instance().getPreviousRoom()){
                GameState.instance().setAdventurersCurrentRoom(nextRoom);
                return "You flee from the enemies back to " + nextRoom + ".\n";
            }
            else if(nextRoom != null){
                return "You try to run " + this.direction + ", but your path is blocked by the enemies in the room!\n";
            }
        }
        if (nextRoom != null) {
            GameState.instance().setAdventurersCurrentRoom(nextRoom);
            return nextRoom.describeOnEntry();  // Use describeOnEntry() here
        } else {
            return "Sorry, you can't go " + this.direction + " from " + currentRoom.getName() + ".\n";
        }
    }
}
