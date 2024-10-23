import java.util.ArrayList;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;

public class GameState {
    public static class IllegalSaveFormatException extends Exception {
        public IllegalSaveFormatException(String e) {
            super(e);
        }
    }

    private static GameState theInstance;
    private Dungeon dungeon;
    private Room adventurersCurrentRoom;
    private HashSet<Room> visitedRooms;

    // New for Zork III
    private ArrayList<Item> inventory;
    private Hashtable<Room, HashSet<Item>> roomContents;

    static synchronized GameState instance() {
        if (theInstance == null) {
            theInstance = new GameState();
        }
        return theInstance;
    }

    private GameState() {
        this.visitedRooms = new HashSet<Room>();
        this.inventory = new ArrayList<Item>(); // Initialize the inventory here
        this.roomContents = new Hashtable<>(); // Initialize roomContents
    }

    void restore(String filename) throws FileNotFoundException, IllegalSaveFormatException, Dungeon.IllegalDungeonFormatException {
        Scanner s = new Scanner(new FileReader(filename));

        if (!s.nextLine().equals("Zork III save data")) {
            throw new IllegalSaveFormatException("Save file not compatible.");
        }

        String dungeonFileLine = s.nextLine();

        if (!dungeonFileLine.startsWith("Dungeon file: ")) {
            throw new IllegalSaveFormatException("No 'Dungeon file:' after version indicator.");
        }

        dungeon = new Dungeon(dungeonFileLine.substring("Dungeon file: ".length()));

        s.nextLine(); // throw away "Room states:"
        String next = s.nextLine();
        while (!next.equals("===")) {
            String beenHere = s.nextLine();
            if (beenHere.endsWith("true")) {
                this.visitedRooms.add(dungeon.getRoom(next));
            }

            String contents = s.nextLine();
            roomContents = new Hashtable<Room, HashSet<Item>>();
            // Restore item that was in room at save-time 
            if (contents.startsWith("Contents:")) {
                contents = contents.substring("Contents: ".length());
                String[] roomContentSplit = contents.split(",");
                if (roomContentSplit.length > 0) {
                    for (String itemName : roomContentSplit) {
                        Item roomItem = dungeon.getItem(itemName.strip());
                        this.addItemToRoom(roomItem, dungeon.getRoom(next));
                    }
                }
            } else {
                this.addItemToRoom(null, dungeon.getRoom(next));
            }

            s.nextLine(); // throw away "---"
        }

        s.nextLine(); // Throw away "Adventurer:"
        String currentRoomLine = s.nextLine();
        adventurersCurrentRoom = dungeon.getRoom(currentRoomLine.substring("Current room: ".length()));

        // New inventory to current room 
        inventory = new ArrayList<Item>();

        String roomInventory = s.nextLine();
        int colon = roomInventory.indexOf(":");
        String newInventory = roomInventory.substring(colon + 1);
        String[] inventoryItems = newInventory.split(",");
        for (String inventoryItem : inventoryItems) {
            this.addToInventory(dungeon.getItem(inventoryItem.strip()));
        }
    }

    void store(String saveName) throws IOException {
        String filename = this.getFullSaveName(saveName);
        PrintWriter w = new PrintWriter(new FileWriter(filename));
        w.println("Zork III save data");
        w.println("Dungeon file: " + this.getDungeon().getTitle());
        w.println("Room states:");

        for (Room visitedRoom : this.visitedRooms) {
            w.println(visitedRoom.getName());
            w.println("beenHere=true");
            w.println("Contents: ");
            for (Item item : this.roomContents.get(visitedRoom)) {
                w.println(item.getPrimaryName() + ",");
            }
            w.println("---");
        }

        w.println("===");
        w.println("Current room: " + this.getAdventurersCurrentRoom().getName());
        w.println("Adventurer:");
        if (this.inventory.size() > 0) {
            w.print("Inventory: ");
            for (Item item : inventory) {
                w.print(item.getPrimaryName() + ",");
            }
        }
        w.close();
    }

    String getFullSaveName(String saveName) {
        if (!saveName.endsWith(".sav")) {
            saveName += ".sav";
        }
        if (!saveName.contains("files")) {
            saveName = "../files/" + saveName;
        }
        return saveName;
    }

    void initialize(Dungeon dungeon) {
        this.dungeon = dungeon;
        adventurersCurrentRoom = dungeon.getEntry();
    }

    Room getAdventurersCurrentRoom() {
        return adventurersCurrentRoom;
    }

    void setAdventurersCurrentRoom(Room room) {
        adventurersCurrentRoom = room;
    }

    Dungeon getDungeon() {
        return dungeon;
    }

    boolean hasBeenVisited(Room r) {
        return this.visitedRooms.contains(r);
    }

    void visit(Room r) {
        this.visitedRooms.add(r);
    }

    ArrayList<Item> getInventory() {
        return this.inventory;
    }

    void addToInventory(Item item) {
        this.inventory.add(item);
    }

    void removeFromInventory(Item item) {
        this.inventory.remove(item);
    }

    Item getItemInVicinityNamed(String name) throws NoItemException {
        Item result = null;
        for (Item item : this.roomContents.get(this.adventurersCurrentRoom)) {
            if (item.getPrimaryName().equals(name)) {
                result = item;
            }
        }
        if (result == null) {
            for (Item item : inventory) {
                if (item.getPrimaryName().equals(name)) {
                    result = item;
                }
            }
        }
        if (result == null) {
            throw new NoItemException();
        }
        return result;
    }

    Item getItemFromInventoryNamed(String name) throws NoItemException {
        for (Item item : inventory) {
            if (item.goesBy(name)) {
                return item;
            }
        }
        throw new NoItemException();
    }

    HashSet<Item> getItemsInRoom(Room room) {
        // Ensure roomContents is initialized properly for each room
        if (!roomContents.containsKey(room)) {
            roomContents.put(room, new HashSet<Item>());
        }
        return this.roomContents.get(room);
    }

    void addItemToRoom(Item item, Room room) {
        if (!roomContents.containsKey(room)) {
            roomContents.put(room, new HashSet<Item>());
        }
        this.roomContents.get(room).add(item);
    }

    void removeItemFromRoom(Item item, Room room) {
        if (roomContents.containsKey(room)) {
            this.roomContents.get(room).remove(item);
        }
    }
}
