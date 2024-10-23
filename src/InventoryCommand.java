class InventoryCommand extends Command {
    @Override
    public String execute() {
        if (GameState.instance().getInventory().isEmpty()) {
            return "Your inventory is empty.\n";
        }

        StringBuilder inventoryList = new StringBuilder("You are carrying:\n");
        for (Item item : GameState.instance().getInventory()) {
            inventoryList.append(item.getPrimaryName()).append("\n");
        }
        return inventoryList.toString();
    }
}
