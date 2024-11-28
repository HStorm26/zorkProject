class InventoryCommand extends Command {
    @Override
    public String execute() {
        int money = GameState.instance().getMoney();
        String moneyName = GameState.instance().getDungeon().getCurrencyName();
        if (GameState.instance().getInventory().isEmpty()) {
            String answer = "Your inventory is empty.\n";
            if(GameState.instance().getDungeon().hasShops()){
                answer += "You have " + money + " " + moneyName + ".\n";
            }
            return answer;
        }

        StringBuilder inventoryList = new StringBuilder("You are carrying:\n");
        for (Item item : GameState.instance().getInventory()) {
            Weapon w;
            try{
                w = GameState.instance().getActiveWeapon();
            }catch(Exception e){
                w = null;
            }
            if(item == w){
                inventoryList.append("(Equipped) ");
            }
            inventoryList.append(item.getPrimaryName()).append("\n");
        }
        if(GameState.instance().getDungeon().hasShops()){
            inventoryList.append("You have " + money + " " + moneyName + ".\n");
        }
        return inventoryList.toString();
    }
}
