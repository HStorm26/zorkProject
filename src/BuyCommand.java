class BuyCommand extends Command{
    
    private String itemName;

    BuyCommand(String command){
        this.itemName = command;
    }

    String execute(){
        try{
            Item item = GameState.instance().getAdventurersCurrentRoom().getShop().getItem(itemName);
            if(item == null){
                return "This shop doesn't have that in stock.\n";
            }
            else if((item.getWeight() + GameState.instance().getInventoryWeight()) >= 40){
                return "You don't have space in your inventory for that.\n";
            }
            else if(item.getPriceFromShop() > GameState.instance().getMoney()){
                return "You can't afford that!\n";
            }
            else{
                GameState.instance().getAdventurersCurrentRoom().getShop().sellItemToPlayer(item);
                return "You are now the proud owner of one " + itemName + ".\n";
            }
        }catch(Exception e){
            return "There's no shop here!\n";
        }
    }
}
