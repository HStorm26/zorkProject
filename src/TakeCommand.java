import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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

        ArrayList<Item> inventory = GameState.instance().getInventory();
        int totalWeight = 0;
        for(int i=0; i<inventory.size(); i++){
            totalWeight += inventory.get(i).getWeight();
        }        
        if(itemName.equals("all") && !currentRoom.getContents().isEmpty()){
            String bigHaul = "";
            Iterator<Item> iterator = GameState.instance().getItemsInRoom(currentRoom).iterator();
            while(iterator.hasNext()){
                Item nextItem = iterator.next();
                if(nextItem.getWeight() + totalWeight > 40){
                    bigHaul += "You can't carry any more weight!\n";
                }
                else{
                    GameState.instance().addToInventory(nextItem);
                    totalWeight += nextItem.getWeight();
                    iterator.remove();
                    bigHaul += "You take the " + nextItem + ".\n";
                }
            }
            return bigHaul;
        }
        else if(currentRoom.getContents().isEmpty()){
            return "There's nothing to take!\n";
        }
        if (item == null) {
            return "There is no " + itemName + " here.\n";
        }
        // Add the item to the player's inventory and remove it from the room
        if(totalWeight + item.getWeight() > 40){
            return "You can't carry any more weight!\n";
        }
        GameState.instance().addToInventory(item);
        currentRoom.remove(item);

        return "You take the " + item.getPrimaryName() + ".\n";
    }
}
