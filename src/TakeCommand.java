class TakeCommand extends Command {
    private String itemName;

    TakeCommand(String itemName) {
        this.itemName = itemName;
    }

    String execute() {
        Room currentRoom = GameState.instance().getAdventurersCurrentRoom();
        Item item = currentRoom.getItemNamed(itemName);

        if (item == null) {
            System.out.println("DEBUG: Item " + itemName + " not found in room " + currentRoom.getName());
            return "There is no " + itemName + " here.\n";
        }

        // Add the item to the player's inventory and remove it from the room
        GameState.instance().addToInventory(item);
        currentRoom.remove(item);

        return "You take the " + item.getPrimaryName() + ".\n";
    }
}
