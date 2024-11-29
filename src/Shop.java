import java.util.HashSet;

public class Shop {
    public static class NoShopException extends Exception {}

    private HashSet<Item> contents = new HashSet<>();
    private double haggleChance;
    private boolean isHaggled;
    //contents is defined somewhere else in the scope of the functions, so it's being weird.
    //I understand now why Stephen loves using this.var because that fixed the error
    
    public Shop(String contents){
        String[] itemNames = contents.split(",");
        Item nextItem;
        //each shop will have a unique chance to be able to be haggled.
        try{
            haggleChance = Double.parseDouble(itemNames[0]);
            for(int i=1; i<itemNames.length; i++){
                nextItem = GameState.instance().getDungeon().getItem(itemNames[i]);
                this.contents.add(nextItem);
            }
        }catch(Exception e){ //just in case you don't want it to be possible.
            haggleChance = 1.0; //1.0 because getRandom() is 0 inclusive but 1 exclusive
            for(int i=0; i<itemNames.length; i++){
                nextItem = GameState.instance().getDungeon().getItem(itemNames[i]);
                this.contents.add(nextItem);
            }
        }
    }
    String describe(){
        String contentList = "\nThere is a shop here.";
        if(!this.contents.isEmpty()){
            contentList += " It sells:\n";
        }
        java.util.Iterator<Item> iter = contents.iterator();
        while(iter.hasNext()){
            Item nextItem = iter.next();
            int price = nextItem.getPriceFromShop();
            if(this.isHaggled){
                price = (int) (price * (0.8));
            }
            contentList += "a " + nextItem.getPrimaryName() + " for " + price + " " + 
             GameState.instance().getDungeon().getCurrencyName();
            if(iter.hasNext()){
                contentList += "\n";
            }
        }
        return contentList;
    }
    Item getItem(String itemName){
        for(Item nextItem : this.contents){
            if(nextItem.goesBy(itemName)){
                return nextItem;
            }
        }
        return null;
    }
    HashSet<Item> getContents(){
        return this.contents;
    }
    void addItemsFromSave(String items){
        this.contents.clear();
        items = items.substring("Shop: ".length());
        String[] list = items.split(",");
        for(int i=0; i<list.length; i++){
            this.contents.add(GameState.instance().getDungeon().getItem(list[i]));
        }
    }
    void sellItemToPlayer(Item item){
        int price = item.getPriceFromShop();
        if(this.isHaggled){
            price = (int) (price * (0.8));
        }
        GameState.instance().addMoney(price * (-1));
        GameState.instance().addToInventory(item);
        this.contents.remove(item);
    }
    void buyItemFromPlayer(Item item){
        GameState.instance().addMoney(item.getPriceFromInventory());
        this.contents.add(item);
        GameState.instance().removeFromInventory(item);
    }
    int agreeToLowerPrice(){
        if(this.isHaggled){
            return 0;
        }
        this.isHaggled = true;
        //you can't haggle a store more than once.
        double attempt = GameState.instance().getRandom();
        if(attempt < this.haggleChance){
           return 1;
        }
        return -1;
    }
}
