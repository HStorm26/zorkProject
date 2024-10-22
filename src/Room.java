import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

public class Room {

    class NoRoomException extends Exception {}

    private String name;
    private String desc;
    private boolean beenHere;
    private ArrayList<Exit> exits;

    Room(String name) {
        this.name = name;
        this.exits = new ArrayList<Exit>();
    }

    Room(Scanner s) throws NoRoomException,
        Dungeon.IllegalDungeonFormatException {

        this.exits = new ArrayList<Exit>();
        name = s.nextLine();
        desc = "";
        if (name.equals("===")) {
            throw new NoRoomException();
        }
        
        String lineOfDesc = s.nextLine();
        while (!lineOfDesc.equals("---") &&
               !lineOfDesc.equals("===")) {
            desc += lineOfDesc + "\n";
            lineOfDesc = s.nextLine();
        }

        if (!lineOfDesc.equals("---")) {
            throw new Dungeon.IllegalDungeonFormatException(
                "No '---' after room.");
        }
    }

    String getName() { return this.name; }

    void setDesc(String desc) { this.desc = desc; }

    public String describe() {
        String description;
        if (GameState.instance().hasBeenVisited(this)) {
            description = this.name;
        } else {
            description = this.name + "\n" + this.desc + "\n";
        }
        for (Exit exit : this.exits) {
            description += "\n" + exit.describe();
        }
        GameState.instance().visit(this);
        return description;
    }
    
    public Room leaveBy(String dir) {
        for (Exit exit : exits) {
            if (exit.getDir().equals(dir)) {
                return exit.getDest();
            }
        }
        return null;
    }

    void addExit(Exit exit) {
        this.exits.add(exit);
    }
    //still needs doing
    HashSet<Item> getContents() {
        return GameState.instance().getItemsInRoom(this);
    }

    Item getItemNamed(String itemName) {
        HashSet<Item> items = this.getContents();
        for(Item item : items) {
            if(item.goesBy(itemName)){
                return GameState.instance().getDungeon().getItem(item.getPrimaryName());
            }
        }
        return null;
    }

    void add(Item item) {
        GameState.instance().addItemToRoom(item, this);
    }

    void remove(Item item) {
        GameState.instance().removeItemFromRoom(item, this);
    }
}
