public class DropCommand extends Command {
    private String itemName;

    public DropCommand(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String execute() {
        try {
            Item item = GameState.instance().getItemFromInventoryNamed(itemName);
            GameState.instance().removeFromInventory(item);
            GameState.instance().addItemToRoom(item, GameState.instance().getAdventurersCurrentRoom());
            return "You have dropped " + item.getPrimaryName() + ".";
        } catch (NoItemException e) {
            return "Item not found in inventory: " + itemName;
        }
    }
}
