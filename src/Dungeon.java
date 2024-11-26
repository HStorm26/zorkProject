import java.util.Hashtable;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Dungeon {
    public static class IllegalDungeonFormatException extends Exception {
        public IllegalDungeonFormatException(String e) {
            super(e);
        }
    }

    private String filename;
    private String title, currencyName;
    private Room entry;
    private Hashtable<String, Room> rooms;
    private Hashtable<String, Item> items;
    private boolean hasShops = false;
    private Hashtable<String, Enemy> enemies;

    Dungeon(String title, Room entry) {
        this.title = title;
        this.entry = entry;
        this.rooms = new Hashtable<>();
        this.items = new Hashtable<>();
        this.enemies = new Hashtable<>();
    }

    public Dungeon(String filename) throws FileNotFoundException, IllegalDungeonFormatException {
        this.filename = filename;
        this.rooms = new Hashtable<>();
        this.items = new Hashtable<>();
        this.enemies = new Hashtable<>();
        Scanner s = new Scanner(new FileReader(filename));

        title = s.nextLine();
        GameState.instance().setDungeon(this);
        s.nextLine(); // Throw away version indicator.

        if (!s.nextLine().equals("===")) {
            throw new IllegalDungeonFormatException("No '===' after version indicator.");
        }

        // Currency name
        String moneyName = s.nextLine();
        if (moneyName.startsWith("Currency Name: ")) {
            this.currencyName = moneyName.substring("Currency Name: ".length());
            s.nextLine(); // throw away "==="
            moneyName = s.nextLine();
            hasShops = true;
        }

        // Items section
        if (!moneyName.equals("Items:")) {
            throw new IllegalDungeonFormatException("No 'Items:' line where expected.");
        }

        try {
            while (true) {
                Item newItem = new Item(s);
                add(newItem);
            }
        } catch (NoItemException e) {
            // End of items
        }

        // Enemies section
        moneyName = s.nextLine();

        if (moneyName.equals("Enemies:")) {
            try {
                while (true) {
                    Enemy newEnemy = new Enemy(s);
                    add(newEnemy);
                }
            } catch (NoEnemyException e) {
                // End of enemies
            }
            moneyName = s.nextLine();
        }

        // Rooms section
        if (!moneyName.equals("Rooms:")) {
            throw new IllegalDungeonFormatException("No 'Rooms:' line where expected.");
        }

        try {
            // Instantiate and add first room (the entry).
            entry = new Room(s);
            add(entry);

            // Instantiate and add other rooms.
            while (true) {
                add(new Room(s));
            }
        } catch (Room.NoRoomException e) {
            // End of rooms
        }
        moneyName = s.nextLine();
        // Exits section
        if (!moneyName.equals("Exits:")) {
            throw new IllegalDungeonFormatException("No 'Exits:' line where expected.");
        }

        try {
            while (true) {
                new Exit(s, this);
            }
        } catch (Exit.NoExitException e) {
            // End of exits
        }

        s.close();
    }

    public Room getEntry() {
        return this.entry;
    }

    public String getTitle() {
        return this.title;
    }

    public Item getItem(String itemName) {
        return items.get(itemName);
    }

    public void add(Room room) {
        this.rooms.put(room.getName(), room);
    }

    public void add(Item item) {
        this.items.put(item.getPrimaryName(), item);
    }

    public void add(Enemy enemy) {
        this.enemies.put(enemy.getName(), enemy);
    }

    public Room getRoom(String roomName) {
        return this.rooms.get(roomName);
    }

    public Enemy getEnemy(String enemyName) {
        return this.enemies.get(enemyName);
    }

    public String getFilename() {
        return this.filename;
    }

    public Hashtable<String, Room> getRooms() {
        return this.rooms;
    }

    public String getCurrencyName() {
        return this.currencyName;
    }

    public boolean hasShops() {
        return this.hasShops;
    }
}
