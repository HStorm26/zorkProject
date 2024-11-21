import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashSet;

public class Room {
    public static class NoRoomException extends Exception {}

    private String name;
    private String desc;
    private ArrayList<Exit> exits;
    private HashSet<Enemy> enemies;
    public Room(String name) {
        this.name = name;
        this.exits = new ArrayList<>();
    }

    public Room(Scanner s) throws  NoRoomException, Dungeon.IllegalDungeonFormatException {
        this.exits = new ArrayList<>();
        this.enemies = new HashSet<Enemy>();
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
        }
            //enemy
            String enemy  = s.nextLine();
             if(enemy.startsWith("Enemies: ")){
                 String[] enemyList = enemy.substring("Enemies: ".length()).split(",");
                 for(int i =0;i<enemyList.length;i++){
                     try{
                         this.addEnemy(GameState.instance().getEnemyNamed(enemyList[i]));
                     }
                     catch(NoEnemyException e){
                 }
             }
             }

             lineOfDesc = s.nextLine();

    
        while (!lineOfDesc.equals("---") && !lineOfDesc.equals("===")) {
            desc += lineOfDesc + "\n";
            lineOfDesc = s.nextLine();
        }

        if (!lineOfDesc.equals("---")) {
            throw new Dungeon.IllegalDungeonFormatException("No '---' after room.");
        }
        System.out.println(this + ": " + this.desc);
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
        for(Enemy e: this.enemies){
            description += "\nThere is a " + e + " here.";
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
        for(Enemy e: this.enemies){
            description += "\nThere is a "+ e + " here.";
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

      public void addEnemy(Enemy enemy){
          this.enemies.add(enemy);
      }
      public void removeEnemy(Enemy enemy) throws NoEnemyException{
          try{
              this.enemies.remove(enemy);
          }
          catch(Exception e){
              throw new NoEnemyException();
          }
      }
      Enemy getEnemyNamed(String enemyName) throws NoEnemyException{
          Enemy result = null;
          for(Enemy e:this.enemies){
              if(e.getName().equals(enemyName)){
                  result = e;
              }
          }
          if(result==null){
              throw new NoEnemyException();
             }
          return result;
      }
      HashSet<Enemy> getAllEnemies(){
          return this.enemies;
      }
      public String toString(){
          return this.name;
      }
}




