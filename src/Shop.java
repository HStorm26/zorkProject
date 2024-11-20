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
            System.out.println(nextItem.getPrimaryName());

        }
    }
    String printContents(){
        String contentList = "\nThere is a shop here. It sells:\n";
        java.util.Iterator<Item> iter = contents.iterator();
        while(iter.hasNext()){
            Item nextItem = iter.next();
            contentList += "a " + nextItem.getPrimaryName() + " for " + nextItem.getBuyPrice()
             + " " + GameState.instance().getDungeon().getCurrencyName();
            if(iter.hasNext()){
                contentList += "\n";
            }
        }
        return contentList;
    }

}
