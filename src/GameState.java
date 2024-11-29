import java.util.ArrayList;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.Random;

public class GameState {
    public static class IllegalSaveFormatException extends Exception {
        public IllegalSaveFormatException(String e) {
            super(e);
        }
    }

    private static GameState theInstance;
    private Dungeon dungeon;
    private Room adventurersCurrentRoom;
    private Room previousRoom;
    private HashSet<Room> visitedRooms;
    private int score, health, money;
    private boolean hasWon, inCombat;
    private Random randomNumber;
    private ArrayList<Item> inventory;
    private Hashtable<Room, HashSet<Item>> roomContents;
    private ArrayList<Enemy> enemies;
    private Weapon activeWeapon;

    static synchronized GameState instance() {
        if (theInstance == null) {
            theInstance = new GameState();
        }
        return theInstance;
    }

    private GameState() {
        this.enemies = new ArrayList<Enemy>();
        this.visitedRooms = new HashSet<Room>();
        this.inventory = new ArrayList<Item>();
        this.roomContents = new Hashtable<>();
        this.score = 0;
        this.money = 0;
        this.health = 100;
        this.hasWon = false;
        this.inCombat = false;
        this.activeWeapon = null;
        this.previousRoom = null;
    }

    void restore(String filename) throws NoEnemyException, FileNotFoundException, IllegalSaveFormatException, Dungeon.IllegalDungeonFormatException {
        Scanner s = new Scanner(new FileReader(filename));

        if (!s.nextLine().equals("Zork++ save data")) {
            throw new IllegalSaveFormatException("Version only compatible with Zork++ saves.");
        }

        String dungeonFileLine = s.nextLine();

        if (!dungeonFileLine.startsWith("Dungeon file: ")) {
            throw new IllegalSaveFormatException("No 'Dungeon file:' after version indicator.");
        }
        dungeon = new Dungeon(dungeonFileLine.substring("Dungeon file: ".length()));
        this.dungeon = dungeon;
        s.nextLine(); // throw away "Room states:"
        String next = s.nextLine();
        next = next.substring(0, next.indexOf(":"));
        while (!next.equals("===")) {
            String beenHere = s.nextLine();
            if (beenHere.endsWith("true")) {
                this.visitedRooms.add(dungeon.getRoom(next));
            }
            String contents = s.nextLine();
            if (contents.startsWith("Contents:")) {
                this.getItemsInRoom(this.getDungeon().getRoom(next)).clear();
                contents = contents.substring("Contents: ".length());
                String[] roomItems = contents.split(",");
                for (int i = 0; i < roomItems.length; i++) {
                    if (roomItems[i].isEmpty()) {
                        break;
                    }
                    this.addItemToRoom(
                        this.getDungeon().getItem(roomItems[i]),
                        this.getDungeon().getRoom(next));
                }
                contents = s.nextLine(); // throw away "---" OR move to enemies/shop contents
            }
            if (contents.startsWith("Enemies: ")) {
                String enemyContents = contents.substring("Enemies: ".length());
                String[] roomEnemies = enemyContents.split(",");
                this.getDungeon().getRoom(next).getAllEnemies().clear();
                for (int i = 0; i < roomEnemies.length; i++) {
                    if (roomEnemies[i].isEmpty()) {
                        break;
                    }
                    Enemy e = this.getDungeon().getEnemy(roomEnemies[i].substring(
                        0, roomEnemies[i].indexOf("(")));
                    this.addEnemyToRoom(e,this.getDungeon().getRoom(next));
                    e.setHealth(Integer.parseInt(roomEnemies[i].substring(
                        roomEnemies[i].indexOf("(")+1, roomEnemies[i].indexOf(")"))));
                }
                contents = s.nextLine(); // throw away "---" OR move to shop contents
            }
            if(contents.startsWith("Shop:")){
                try{
                    this.getDungeon().getRoom(next).getShop().addItemsFromSave(contents);
                }catch(Exception e){}
                s.nextLine(); //throw away "---"
            }
            next = s.nextLine();
            if (!next.equals("===")) {
                next = next.substring(0, next.indexOf(":"));
            }
        }

        s.nextLine(); // Throw away "Adventurer:"
        next = s.nextLine();
        adventurersCurrentRoom = dungeon.getRoom(next.substring("Current room: ".length()));

        inventory = new ArrayList<Item>();
        next = s.nextLine();
        if (next.startsWith("Inventory:")) {
            try {
                String playerInventory = next;
                playerInventory = playerInventory.substring("Inventory: ".length());
                String[] inventoryItems = playerInventory.split(",");
                for (String inventoryItem : inventoryItems) {
                    this.addToInventory(dungeon.getItem(inventoryItem.strip()));
                    Iterator<Room> iterator = roomContents.keySet().iterator();
                    while (iterator.hasNext()) {
                        Room inventoryRoom = iterator.next();
                        roomContents.get(inventoryRoom).remove(
                            this.getDungeon().getItem(inventoryItem.strip()));
                    }
                }
            } catch (Exception e) {}
            next = s.nextLine();
        }
        this.health = Integer.parseInt(next.substring("Current health: ".length()));
        next = s.nextLine();
        this.score = Integer.parseInt(next.substring("Current score: ".length()));
        next = s.nextLine();
        this.money = Integer.parseInt(next.substring("Current money: ".length()));
        next = s.nextLine();
        next = next.substring("Equipped weapon: ".length());
        this.activeWeapon = (Weapon) this.getDungeon().getItem(next);
    }

    void store(String saveName) throws IOException {
        String filename = this.getFullSaveName(saveName);
        PrintWriter w = new PrintWriter(new FileWriter(filename));
        w.println("Zork++ save data");
        w.println("Dungeon file: " + this.getDungeon().getFilename());
        w.println("Room states:");

        for (Room visitedRoom : this.visitedRooms) {
            w.println(visitedRoom.getName() + ":");
            w.println("beenHere=true");
            w.print("Contents: ");
            Iterator<Item> iterator = this.roomContents.get(visitedRoom).iterator();
            while (iterator.hasNext()) {
                Item item = iterator.next();
                w.print(item.getPrimaryName());
                if (iterator.hasNext()) {
                    w.print(",");
                }
            }
            w.print("\n");
            w.print("Enemies: ");
            Iterator<Enemy> enemyIterator = visitedRoom.getAllEnemies().iterator();
            while (enemyIterator.hasNext()) {
                Enemy e = enemyIterator.next();
                w.print(e.getName() + "(" + e.getHealth() + ")");
                if (enemyIterator.hasNext()) {
                    w.print(",");
                }
            }
            try{
                iterator = visitedRoom.getShop().getContents().iterator();
                w.print("\nShop: ");
                while(iterator.hasNext()){
                    Item item = iterator.next();
                    w.print(item.getPrimaryName());
                    if(iterator.hasNext()){
                        w.print(",");
                    }
                }
            }catch(Exception e){}
            w.print("\n");
            w.println("---");
        }

        w.println("===");
        w.println("Adventurer:");
        w.println("Current room: " + this.getAdventurersCurrentRoom().getName());
        if (this.inventory.size() > 0) {
            String inventoryList = "Inventory: ";
            for (Item item : inventory) {
                inventoryList += (item.getPrimaryName() + ",");
            }
            inventoryList = inventoryList.substring(0, inventoryList.length() - 1);
            w.println(inventoryList);
        }
        w.println("Current health: " + this.health);
        w.println("Current score: " + this.score);
        w.println("Current money: " + this.money);
        w.println("Equipped weapon: " + this.activeWeapon.getPrimaryName());
        w.flush();
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
        this.previousRoom = adventurersCurrentRoom;
        adventurersCurrentRoom = room;
    }

    Room getPreviousRoom() {
        return this.previousRoom;
    }

    Dungeon getDungeon() {
        return dungeon;
    }

    void setDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
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

    int getInventoryWeight() {
        int totalWeight = 0;
        for (Item nextItem : this.inventory) {
            totalWeight += nextItem.getWeight();
        }
        return totalWeight;
    }

    void addToInventory(Item item) {
        this.inventory.add(item);
    }

    void removeFromInventory(Item item) {
        if(item == this.activeWeapon){
            this.setActiveWeapon(null);
        }
        this.inventory.remove(item);
    }

    Item getItemInVicinityNamed(String name) throws NoItemException {
        Item result = null;
        HashSet<Item> hash = this.getItemsInRoom(this.adventurersCurrentRoom);
        for (Item item : hash) {
            if (item.goesBy(name)) {
                result = item;
            }
        }
        if (result == null) {
            for (Item item : inventory) {
                if (item.goesBy(name)) {
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
        if (!roomContents.containsKey(room)) {
            roomContents.put(room, new HashSet<Item>());
        }
        return this.roomContents.get(room);
    }

    void addItemToRoom(Item item, Room room) {
        if (!this.roomContents.containsKey(room)) {
            this.roomContents.put(room, new HashSet<Item>());
        }
        this.roomContents.get(room).add(item);
    }

    void removeItemFromRoom(Item item, Room room) {
        if (roomContents.containsKey(room)) {
            this.roomContents.get(room).remove(item);
        }
    }

    void addScore(int points) {
        this.score += points;
    }

    int getScore() {
        return score;
    }

    void woundPlayer(int damage) {
        this.health -= damage;
        if (health <= 0) {
            this.killPlayer();
        }
        if (health > 100) {
            this.health = 100;
        }
    }

    int getHealth() {
        return health;
    }

    boolean checkIfDead() {
        if (health <= 0) {
            return true;
        } else {
            return false;
        }
    }

    void killPlayer() {
        this.health = 0;
    }

    boolean checkIfWon() {
        return hasWon;
    }

    void winGame() {
        this.hasWon = true;
    }

    public double getRandom() {
        if (randomNumber == null) {
            randomNumber = new Random(12);
        }
        return randomNumber.nextDouble();
    }
    
    int getMoney(){
        return this.money;
    }

    void addMoney(int c){
        this.money += c;
    }

    public void addEnemy(Enemy enemy) {
        this.enemies.add(enemy);
    }

    public void addEnemyToRoom(Enemy enemy, Room room) {
        room.addEnemy(enemy);
    }

    Weapon getActiveWeapon() throws NoItemException{
        if(this.activeWeapon == null){
            throw new NoItemException();
        }
        return this.activeWeapon;
    }
    void setActiveWeapon(Weapon w) {
        this.activeWeapon = w;
    }
    boolean combatUpdate(){
        if(this.adventurersCurrentRoom.getAllEnemies().isEmpty()){
            this.inCombat = false;
        }
        else{
            this.inCombat = true;
        }
        return inCombat;
    }
    void passiveEnemyHeal(Hashtable<String,Enemy> ht){
        if(!inCombat){    
            Iterator<String> iter = ht.keySet().iterator();
            while(iter.hasNext()){
                String key = iter.next();
                ht.get(key).takeDamage(-1);
            }
        }
    }
}
