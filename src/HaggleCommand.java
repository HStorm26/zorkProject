class HaggleCommand extends Command{

    String execute(){
        Shop shop;
        int result;
        try{
            shop = GameState.instance().getAdventurersCurrentRoom().getShop();
            result = shop.agreeToLowerPrice();
            switch(result){
                case 0:
                    return "The shopkeeper refuses to be bargained with again.\n";
                case 1:
                    return "After a short conversation, the shopkeeper agrees the prices are a bit too high.\nAll prices at this shop are reduced by 20%.\n";
                case -1:
                    return "You are unable to convince the shopkeeper to lower their prices.\n";
            }
            return "error.\n";
        }catch(Exception e){
            return "There's no shopkeeper to bargain with here!\n";
        }
    }
}
