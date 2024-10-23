import java.util.ArrayList;
public class DropCommand extends Command {
    private String itemName;

    public DropCommand(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String execute() {
        try {
            if(itemName.equals("")){ return "drop what?"; }
            Room currentRoom = GameState.instance().getAdventurersCurrentRoom();
            ArrayList<Item> inventory= GameState.instance().getInventory();
            if(itemName.equals("all") && !inventory.isEmpty()){
                String bigDesc = "";
                while(!inventory.isEmpty()){
                   GameState.instance().addItemToRoom(inventory.get(0), currentRoom);
                   bigDesc += "You have dropped the " + inventory.get(0) + ".\n";
                   inventory.remove(0);
                }
                return bigDesc;
            }
            else if(inventory.isEmpty()){
                return "You're not carrying anything!\n";
            }
            Item item = GameState.instance().getItemFromInventoryNamed(itemName);
            
            GameState.instance().removeFromInventory(item);
            GameState.instance().addItemToRoom(item, GameState.instance().getAdventurersCurrentRoom());
            return "You have dropped the " + item.getPrimaryName() + ".\n";
        } catch (NoItemException e) {
            return "You're not carrying a " + itemName + ".\n";
        }
    }
}
