import java.util.ArrayList;
class TakeCommand extends Command {
    private String itemName;

    TakeCommand(String itemName) {
        this.itemName = itemName;
    }

    String execute() {
        if(itemName.equals("")){
            return "take what?";
        }
        Room currentRoom = GameState.instance().getAdventurersCurrentRoom();
        Item item = currentRoom.getItemNamed(itemName);

        if (item == null) {
            return "There is no " + itemName + " here.\n";
        }
        ArrayList<Item> items = GameState.instance().getInventory();
        int totalWeight = 0;
        for(int i=0; i<items.size(); i++){
            totalWeight += items.get(i).getWeight();
        }
        if(totalWeight + item.getWeight() > 40){
            return "You can't carry any more weight!\n";
        }
        // Add the item to the player's inventory and remove it from the room
        

        GameState.instance().addToInventory(item);
        currentRoom.remove(item);

        return "You take the " + item.getPrimaryName() + ".\n";
    }
}
