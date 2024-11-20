class SellCommand extends Command{

    private String itemName;

    SellCommand(String command){
        this.itemName = command;
    }

    String execute(){
        if(itemName.equals("")){
            return "sell what?\n";
        }
        Item item;
        Shop shop;
        try{
            item = GameState.instance().getItemFromInventoryNamed(itemName);
        }catch(Exception e){
            return "You don't have that item to sell!\n";
        }
        try{
            shop = GameState.instance().getAdventurersCurrentRoom().getShop();
        }catch(Exception e){
            return "There's no shop here to sell to!\n";
        }
        if(item.getPriceFromInventory() == 0){
            //"key" items (items players need for progression) will not be able to be sold.
            //they should have their prices set to zero.
            return "You're not going to want to sell the " + item.getPrimaryName() + ".\n";
        }
        else{
            shop.buyItemFromPlayer(item);
            return "You have parted with the " + item.getPrimaryName() + " in exchange for some money.\n";
        }
    }
}
