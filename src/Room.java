import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Room {
    public static class NoRoomException extends Exception {}

    private String name;
    private String desc;
    private ArrayList<Exit> exits;
    private Shop shop;
    private HashSet<Enemy> enemies;

    public Room(String name) {
        this.name = name;
        this.exits = new ArrayList<>();
        this.shop = null;
        this.enemies = new HashSet<>();
    }

    public Room(Scanner s) throws NoRoomException, Dungeon.IllegalDungeonFormatException {
        this.exits = new ArrayList<>();
        this.shop = null;
        this.enemies = new HashSet<>();
        name = s.nextLine();
        desc = "";
        if (name.equals("===")) {
            throw new NoRoomException();
        }

        String lineOfDesc = s.nextLine();
        if (lineOfDesc.startsWith("Contents: ")) {
            String[] items = lineOfDesc.substring("Contents: ".length()).split(",");
            for (int i = 0; i < items.length; i++) {
                this.add(GameState.instance().getDungeon().getItem(items[i]));
            }
            lineOfDesc = s.nextLine();
        }

        if (lineOfDesc.startsWith("Shop: ")) {
            this.shop = new Shop(lineOfDesc.substring("Shop: ".length()));
            lineOfDesc = s.nextLine();
        }

        if (lineOfDesc.startsWith("Enemies: ")) {
            String[] enemyList = lineOfDesc.substring("Enemies: ".length()).split(",");
            for (int i = 0; i < enemyList.length; i++) {
                this.addEnemy(GameState.instance().getDungeon().getEnemy(enemyList[i]));
            }
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

    public String describeOnEntry() {
        String description;
        HashSet<Item> items = this.getContents();
        if (GameState.instance().hasBeenVisited(this)) {
            description = this.name;
        } else {
            description = this.name + "\n" + this.desc + "\n";
        }
        for (Item item : items) {
            description += "\nThere is a " + item + " here.";
        }
        if (this.shop != null) {
            description += this.shop.describe();
        }
        if (!this.enemies.isEmpty()){
            description += "\nLook out! There are enemies here!";
            for (Enemy e : this.enemies) {
                description += "\nThere is a " + e + " here!";
            }
            description += "\n";
        }
        for (Exit exit : this.exits) {
            description += "\n" + exit.describe();
        }
        GameState.instance().visit(this);
        return description + "\n";
    }

    public String describeFull() {
        String description = this.name + "\n" + this.desc + "\n";
        HashSet<Item> items = this.getContents();
        for (Item item : items) {
            description += "\nThere is a " + item + " here.";
        }
        if (!this.enemies.isEmpty()){
            description += "\nLook out! There are enemies here!";
            for (Enemy e : this.enemies) {
                description += "\nThere is a " + e + " here!";
            }
            description += "\n";
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

    public Shop getShop() throws Shop.NoShopException {
        if (this.shop == null) {
            throw new Shop.NoShopException();
        }
        return this.shop;
    }

    public void addEnemy(Enemy enemy) {
        this.enemies.add(enemy);
    }

    public void removeEnemy(Enemy enemy) throws NoEnemyException {
        if (!this.enemies.remove(enemy)) {
            throw new NoEnemyException();
        }
    }

    public Enemy getEnemyNamed(String enemyName) throws NoEnemyException {
        for (Enemy e : this.enemies) {
            if (e.getName().equals(enemyName)) {
                return e;
            }
        }
        throw new NoEnemyException();
    }

    public HashSet<Enemy> getAllEnemies() {
        return this.enemies;
    }

    public String toString() {
        return this.name;
    }
}
