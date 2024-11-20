import java.util.Hashtable;
import java.util.HashSet;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Iterator;

public class Item {

    private String primaryName;
    private int weight;
    private Hashtable<String, String> messages;
    private HashSet<String> aliases;
    private Hashtable<String,ArrayList<String>> actions;
    private int price;

    public Item(Scanner s) throws NoItemException {
        this.aliases = new HashSet<>();
        this.messages = new Hashtable<>();
        this.actions = new Hashtable<>();
        String next = s.nextLine().strip();
        if (next.equals("Items:")) {
            next = s.nextLine();
        }
        if (!next.equals("===")) {
            String[] splits = next.split(",");
            this.primaryName = splits[0];

            for (String split : splits) {
                this.aliases.add(split.toLowerCase());
            }

            next = s.nextLine();
            this.weight = Integer.parseInt(next);
            next = s.nextLine();
            if(GameState.instance().getDungeon().hasShops()){
                this.price = Integer.parseInt(next);
                next = s.nextLine();
            }

            while (!next.equals("---")) {
                boolean hasActions = false;
                int colon = next.indexOf(":");
                String verb = next.substring(0, colon);
                if(verb.contains("[")){
                    hasActions = true;
                    verb = verb.substring(0, verb.indexOf("["));
                }
                String mssg = next.substring(colon + 1);
                this.messages.put(verb, mssg);

                if(hasActions){
                    actions.put(verb,new ArrayList<>());
                    String[] actionArray = next.substring((next.indexOf("[") + 1), next.indexOf("]")).split(",");
                    actions.get(verb).addAll(java.util.Arrays.asList(actionArray));
                }
                next = s.nextLine();
            }
        } else {
            throw new NoItemException();
        }
    }

    public boolean goesBy(String name) {
        return this.aliases.contains(name.toLowerCase());
    }

    public String getPrimaryName() {
        return this.primaryName;
    }

    public String getAliases() {
        return this.aliases.toString(); // Debugging: return aliases as a string
    }

    public String getMessageForVerb(String verb) {
        return this.messages.get(verb);
    }

    public int getWeight() {
        return this.weight;
    }

    @Override
    public String toString() {
        return primaryName;
    }

    public int getPriceFromInventory(){
        return price;
    }
    public int getPriceFromShop(){
        return price * 2;
    }
    public void executeActionsForVerb(String verb){
        if(actions.get(verb) == null) {}
        else{
            for(int i=0; i<actions.get(verb).size(); i++){
                String prefix = actions.get(verb).get(i).substring(0, 3);
                //all of the item events have unique combinations of letters at the beginning.
                //we can check these against a set and use a switch case instead of a
                //less efficient if-else chain.
                String fullAction = actions.get(verb).get(i);
                switch(prefix){
                    case "Sco": //Score(x) event
                        int pointsToAdd = Integer.parseInt(fullAction.substring(
                         (fullAction.indexOf("(") + 1), fullAction.indexOf(")")));
                        GameState.instance().addScore(pointsToAdd);
                        actions.get(verb).remove(i);
                        i--;
                        break;
                        //actions can only add points once. repeated use will not grant more
                        //points.
                    case "Wou": //Wound(x) event
                        int damage = Integer.parseInt(fullAction.substring(
                         (fullAction.indexOf("(") + 1), fullAction.indexOf(")")));
                        GameState.instance().woundPlayer(damage);
                        break;
                        //unlike score events, you can continue to injure yourself
                        //repeatedly. That edge isn't signficantly duller between individual cuts.
                    case "Die": //Die event
                        GameState.instance().killPlayer();
                        break;
                    case "Win": //Win event
                        GameState.instance().winGame();
                        break;
                    case "Dro": //Drop event
                        try{ //ignores the drop command if item isn't in your inventory.
                            GameState.instance().removeFromInventory(this);
                            GameState.instance().addItemToRoom(this,
                             GameState.instance().getAdventurersCurrentRoom());
                        } catch(Exception e){}
                        break;
                    case "Dis": //Disappear event
                        try{
                            GameState.instance().removeFromInventory(this);
                        } catch(Exception e){}
                        GameState.instance().removeItemFromRoom(this,
                         GameState.instance().getAdventurersCurrentRoom());
                        break;
                    case "Tra": //Transform event
                        String newName = fullAction.substring(fullAction.indexOf("(") + 1,
                         fullAction.indexOf(")"));
                        Item newItem = GameState.instance().getDungeon().getItem(newName);
                        try{ //if the item is in the player's inventory, transform from there
                            GameState.instance().getItemFromInventoryNamed(this.primaryName);
                            GameState.instance().removeFromInventory(this);
                            int totalWeight = 0;
                            for(int a=0; a<GameState.instance().getInventory().size(); a++){
                                totalWeight += GameState.instance().getInventory().get(a).getWeight();
                            }
                            if(totalWeight + newItem.getWeight() > 40){
                                GameState.instance().addItemToRoom(newItem, GameState.instance()
                                 .getAdventurersCurrentRoom());
                            }
                            else{
                                GameState.instance().addToInventory(newItem);
                            }
                        } catch(Exception e){ //if the item is in the room, transform from there
                            GameState.instance().removeItemFromRoom(this, GameState.instance()
                             .getAdventurersCurrentRoom());
                            GameState.instance().addItemToRoom(newItem, GameState.instance()
                             .getAdventurersCurrentRoom());
                        }
                        break;
                    case "Tel": //Teleport event
                        Hashtable<String,Room> allRooms = 
                         GameState.instance().getDungeon().getRooms();
                        int shuffle = (int) (GameState.instance().getRandom() * allRooms.size());
                        String dest = "";
                        Iterator<String> iter = allRooms.keySet().iterator();
                        for(int a=0; a<=shuffle; a++){
                            dest = iter.next();
                        }
                        GameState.instance()
                         .setAdventurersCurrentRoom(allRooms.get(dest));
                        GameState.instance().visit(allRooms.get(dest));
                        break;
                    default:
                        System.out.println("error.");
                        for(int a=0; i<actions.get(verb).size(); i++){
                            System.out.println(actions.get(verb).get(a));
                        }
                }
            }

        }    
    }
}
