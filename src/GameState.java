import java.util.ArrayList;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Iterator;
import java.util.HashSet;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Enumeration;
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

    //new for Zork III
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
    }

    void restore(String filename) throws FileNotFoundException,
         IllegalSaveFormatException, Dungeon.IllegalDungeonFormatException {

             Scanner s = new Scanner(new FileReader(filename));

             if (!s.nextLine().equals("Zork III save data")) {
                 throw new IllegalSaveFormatException("Save file not compatible.");
             }

             String dungeonFileLine = s.nextLine();

             if (!dungeonFileLine.startsWith("Dungeon file: ")) {
                 throw new IllegalSaveFormatException("No 'Dungeon file:' after version indicator.");
             }

             dungeon = new Dungeon(dungeonFileLine.substring(
                         "Dungeon file: ".length()));

             s.nextLine();   // throw away "Room states:"
             String next = s.nextLine();
             while (!next.equals("===")) {
                

                    String beenHere =s.nextLine();
                    if(beenHere.endsWith("true")){
                         this.visitedRooms.add(dungeon.getRoom(next));
                    }

                String contents = s.nextLine();
                roomContents= new Hashtable<Room,HashSet<Item>>();
                 //restore item that was in room at save-time 
                 if(contents.startsWith("Contents:")){
                   
                    contents = contents.substring("Contents: " .length());
                    String[] roomContentSplit = contents.split(",");
                    if(roomContentSplit.length>0){
                    for(int i =0; i<roomContentSplit.length;i++){
                       Item roomItem = dungeon.getItem(roomContentSplit[i]);
                       this.addItemToRoom(roomItem,dungeon.getRoom(next));         

                 }

               
               }
             }
                 else{
                     this.addItemToRoom(null,dungeon.getRoom(next));
                 }

                 s.nextLine();  // throw away "---"
              
             }
             
             s.nextLine(); //throw away "Adventurer:"
             String currentRoomLine = s.nextLine();
             adventurersCurrentRoom = dungeon.getRoom(
                     currentRoomLine.substring("Current room: ".length()));
             //new inventory to current room 

             inventory = new ArrayList<Item>();

             String roomInventory = s.nextLine();

             int colon = roomInventory.indexOf(":");
             String newInventory = roomInventory.substring(colon + 1);
             String[] inventoryItems = newInventory.split(",");
             for(int i = 0;i<inventoryItems.length;i++){
                this.addToInventory(dungeon.getItem(inventoryItems[i].strip()));
             }

         }

    void store(String saveName) throws IOException {
        String filename = this.getFullSaveName(saveName);
        PrintWriter w = new PrintWriter(new FileWriter(filename));
        w.println("Zork III save data");
        w.println("Dungeon file: " + this.getDungeon().getFilename());
        w.println("Room states:");


        
        Iterator<Room> visitedRoomsIter = this.visitedRooms.iterator();
        while (visitedRoomsIter.hasNext()){
            Room visitedRoom = visitedRoomsIter.next();
            w.println(visitedRoom.getName());
            w.println("beenHere=true");
     
            String contents = "Contents: ";

            if(this.roomContents.get(visitedRoom).size()>0){
            for(Item item: this.roomContents.get(visitedRoom)){
                contents += item.getPrimaryName() + ",";
            }
            }
            w.println(contents);

        }
            w.println("---");
        

        
        w.println("===");
        w.println("Current room: " + this.getAdventurersCurrentRoom().getName());
       w.println("Adventurer:");
        w.println("Current room: " +
                this.getAdventurersCurrentRoom().getName());
        if(this.inventory.size()>0){
        w.print("Inventory: ");
        for(Item item: inventory){
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

    ArrayList<Item> getInventory(){
        return this.inventory;
    }

    void addToInventory(Item item){
        this.inventory.add(item);
    }

    void removeFromInventory(Item item){
        this.inventory.remove(item);
    }

    Item getItemInVicinityNamed(String name)throws NoItemException{
        Item result = null;
        for(Item item: this.roomContents.get(this.adventurersCurrentRoom)){
            if(item.getPrimaryName().equals(name)){
               result = item;
            }
        }
        if(result ==null){
        for(int i = 0; i<inventory.size();i++){
            if(inventory.get(i).getPrimaryName().equals(name)){
                result = inventory.get(i);
            }

        }
       }
        if(result ==null){
            throw new NoItemException();
      
        }
       return result;

    }

    Item getItemFromInventoryNamed(String name)throws NoItemException{
         Item result = null;
        for(int i = 0; i<inventory.size();i++){
            if(inventory.get(i).getPrimaryName().equals(name)){
                result = inventory.get(i);
            }
            else if(i==inventory.size()-1){
                throw new NoItemException();


            }
        }
        return result;


    }
    HashSet<Item> getItemsInRoom(Room room){
        return this.roomContents.get(room);
    }

    void addItemToRoom(Item item,Room room){
        this.roomContents.get(room).add(item);
    }
    void removeItemFromRoom(Item item,Room room){
        this.roomContents.get(room).remove(item);

   }


}
