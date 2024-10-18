
import java.util.Scanner;
import java.util.Iterator;
import java.util.HashSet;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

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
            throw new IllegalSaveFormatException("No '" +
                "Dungeon file: " + 
                "' after version indicator.");
        }

        dungeon = new Dungeon(dungeonFileLine.substring(
            "Dungeon file: ".length()));

        s.nextLine();   // throw away "Room states:"
        String visitedRoomName = s.nextLine();
        while (!visitedRoomName.equals("===")) {
            this.visitedRooms.add(dungeon.getRoom(visitedRoomName));
            s.nextLine();   // throw away "beenHere=true"
            s.nextLine();   // throw away "---"
            visitedRoomName = s.nextLine();
        }
        String currentRoomLine = s.nextLine();
        adventurersCurrentRoom = dungeon.getRoom(
            currentRoomLine.substring("Current room: ".length()));
    }

    void store(String saveName) throws IOException {
        String filename = this.getFullSaveName(saveName);
        PrintWriter w = new PrintWriter(new FileWriter(filename));
        w.println("Zork II save data");
        w.println("Dungeon file: " + this.getDungeon().getFilename());
        w.println("Room states:");
        Iterator<Room> visitedRoomsIter = this.visitedRooms.iterator();
        while (visitedRoomsIter.hasNext()) {
            Room visitedRoom = visitedRoomsIter.next();
            w.println(visitedRoom.getName());
            w.println("beenHere=true");
            w.println("---");
        }
        w.println("===");
        w.println("Current room: " +
            this.getAdventurersCurrentRoom().getName());
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
}
