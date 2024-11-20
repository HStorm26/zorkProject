import java.util.HashSet;

public class Shop {
    public static class NoShopException extends Exception {}

    private HashSet<Item> contents = new HashSet<>();
    
    public Shop(String contents){
        String[] itemNames = contents.split(",");
        Item nextItem;
        for(int i=0; i<itemNames.length; i++){
            nextItem = GameState.instance().getDungeon().getItem(itemNames[i]);
            this.contents.add(nextItem);

        }
    }
    String describe(){
        String contentList = "\nThere is a shop here. It sells:\n";
        java.util.Iterator<Item> iter = contents.iterator();
        while(iter.hasNext()){
            Item nextItem = iter.next();
            contentList += "a " + nextItem.getPrimaryName() + " for " + nextItem.getPriceFromShop()
             + " " + GameState.instance().getDungeon().getCurrencyName();
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
    void sellItemToPlayer(Item item){
        GameState.instance().addMoney(item.getPriceFromShop() * (-1));
        GameState.instance().addToInventory(item);
        this.contents.remove(item);
    }
    void buyItemFromPlayer(Item item){
        GameState.instance().addMoney(item.getPriceFromInventory());
        this.contents.add(item);
        GameState.instance().removeFromInventory(item);
    }
}
