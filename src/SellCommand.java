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
            shop = GameState.instance().getAdventurersCurrentRoom().getShop();
        }catch(Exception e){
            return "There's nobody here for you to sell your items to.\n";
        }
        try{
            item = GameState.instance().getItemFromInventoryNamed(itemName);
        }catch(Exception e){
            return "The shopkeeper refuses your offer of an invisible " + itemName + ".\n";
        }
        if(item.getPriceFromInventory() == 0){
            //"key" items (items players need for progression) will not be able to be sold.
            //they should have their prices set to zero.
            return "As the shopkeeper examines the " + item.getPrimaryName() + ", a strange shiver goes down your spine.\nYou call off the trade, unable to explain why.\n";
        }
        else{
            shop.buyItemFromPlayer(item);
            return "The shopkeeper takes your " + item.getPrimaryName() + " and hands you a bag with the " + GameState.instance().getDungeon().getCurrencyName() + ". They seemed pleased with the trade.\n";
        }
    }
}
