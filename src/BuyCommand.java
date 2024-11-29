class BuyCommand extends Command{
    
    private String itemName;

    BuyCommand(String command){
        this.itemName = command;
    }

    String execute(){
        if(itemName.equals("")){
            return "buy what?\n";
        }
        try{
            Item item = GameState.instance().getAdventurersCurrentRoom().getShop().getItem(itemName);
            if(GameState.instance().combatUpdate()){
                return "The shopkeeper is nowhere to be found!\n";
            }
            if(item == null){
                return "The shopkeeper shakes their head. They don't have that item for sale.\n";
            }
            else if((item.getWeight() + GameState.instance().getInventoryWeight()) >= 40){
                return "The shopkeeper points out that you can't carry this and everything else you already have.\n";
            }
            else if(item.getPriceFromShop() > GameState.instance().getMoney()){
                return "The shopkeeper isn't interested in parting with that item for less than it's worth.\n";
            }
            else{
                GameState.instance().getAdventurersCurrentRoom().getShop().sellItemToPlayer(item);
                return "The shopkeeper takes your money and gladly hands over the " + item.getPrimaryName() + ".\n";
            }
        }catch(Exception e){
            return "There's no shop here to buy anything from.\n";
        }
    }
}
