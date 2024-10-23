import java.util.HashSet;
import java.util.ArrayList;
import java.util.Scanner;

public class Room {
    public static class NoRoomException extends Exception {}

    private String name;
    private String desc;
    private ArrayList<Exit> exits;
    private HashSet<Item> items;

    public Room(String name) {
        this.name = name;
        this.exits = new ArrayList<>();
        this.items = new HashSet<>();
    }

    public Room(Scanner s) throws NoRoomException, Dungeon.IllegalDungeonFormatException {
        this.exits = new ArrayList<>();
        this.items = new HashSet<>();
        name = s.nextLine();
        desc = "";
        if (name.equals("===")) {
            throw new NoRoomException();
        }

        String lineOfDesc = s.nextLine();
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
}
