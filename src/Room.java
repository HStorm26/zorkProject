import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashSet;

public class Room {
    public static class NoRoomException extends Exception {}

    private String name;
    private String desc;
    private ArrayList<Exit> exits;
    private Shop shop;

    public Room(String name) {
        this.name = name;
        this.exits = new ArrayList<>();
    }

    public Room(Scanner s) throws NoRoomException, Dungeon.IllegalDungeonFormatException {
        this.exits = new ArrayList<>();
        this.shop = null;
        name = s.nextLine();
        desc = "";
        if (name.equals("===")) {
            throw new NoRoomException();
        }

        String lineOfDesc = s.nextLine();
        if(lineOfDesc.startsWith("Contents: ")){
            String[] items = lineOfDesc.substring("Contents: ".length()).split(",");
            for(int i=0; i<items.length; i++){
                this.add(GameState.instance().getDungeon().getItem(items[i]));
            }
            lineOfDesc = s.nextLine();
        }
        if(lineOfDesc.startsWith("Shop: ")){
            System.out.println("Nice! we found it!");
            this.shop = new Shop(lineOfDesc.substring("Shop: ".length()));
            lineOfDesc = s.nextLine();
        }
        while (!lineOfDesc.equals("---") && !lineOfDesc.equals("===")) {
            desc += lineOfDesc + "\n";
            lineOfDesc = s.nextLine();
        }

        if (!lineOfDesc.equals("---")) {
            throw new Dungeon.IllegalDungeonFormatException("No '---' after room.");
        }
    }

    public String getName() {
        return this.name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    // Called when entering the room
    public String describeOnEntry() {
        String description;
        HashSet<Item> items = this.getContents();
        if (GameState.instance().hasBeenVisited(this)) {
            description = this.name;  // Only show room name after the first visit
        } else {
            description = this.name + "\n" + this.desc + "\n";  // Full description on first entry
        }
        for (Item item : items) {
            description += "\nThere is a " + item + " here.";
        }
        if(this.shop != null){
            description += this.shop.printContents();
        }
        for (Exit exit : this.exits) {
            description += "\n" + exit.describe();
        }
        GameState.instance().visit(this);  // Mark the room as visited
        return description + "\n";
    }

    // Called by the "look" command
    public String describeFull() {
        String description = this.name + "\n" + this.desc + "\n";  // Full description regardless of visit status
        HashSet<Item> items = this.getContents();
        for (Item item : items) {
            description += "\nThere is a " + item + " here.";
        }
        for (Exit exit : this.exits) {
            description += "\n" + exit.describe();
        }
        return description + "\n";
    }

    public Room leaveBy(String dir) {
        for (Exit exit : exits) {
            if (exit.getDir().equals(dir)) {
                return exit.getDest();
            }
        }
        return null;
    }

    public void addExit(Exit exit) {
        this.exits.add(exit);
    }

    public void add(Item item) {
        GameState.instance().addItemToRoom(item, this);
    }

    public void remove(Item item) {
        GameState.instance().removeItemFromRoom(item, this);
    }

    public HashSet<Item> getContents() {
        return GameState.instance().getItemsInRoom(this);
    }

    public Item getItemNamed(String itemName) {
        HashSet<Item> roomItems = this.getContents();
        for (Item item : roomItems) {
            if (item.goesBy(itemName)) {
                return item;
            }
        }
        return null;
    }
    public Shop getShop() throws Shop.NoShopException{
        if(this.shop == null){
            throw new Shop.NoShopException();
        }
        return this.shop;
    }
}
