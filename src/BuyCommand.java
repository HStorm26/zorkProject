class BuyCommand extends Command{
    
    private String itemName;

    BuyCommand(String command){
        this.itemName = command;
    }

    String execute(){
        try{
            GameState.instance().getAdventurersCurrentRoom().getShop();
            return "me when i try to buy the " + itemName;
        }catch(Exception e){
            return "no shop here!";
        }
    }
}
