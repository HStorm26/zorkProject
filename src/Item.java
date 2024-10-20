import java.util.Hashtable;
import java.util.HashSet;
import java.util.Scanner;
public class Item {

    class NoItemException extends Exception {}

    private String primaryName;
    private int weight;
    private Hashtable<String,String> messages = new Hashtable<>();
    private HashSet<String> aliases = new HashSet<>();

    public Item(Scanner s) {
        String name = s.nextLine();
        if (name.equals("===")) {
            throw new NoItemException();
        }

        String[] names = name.split(",");
        String[] actions = new String[2];
        primaryName = names[0];
        for(int i=0; i<names.length; i++) {
            aliases.put(names[i]);
        }
        weight = s.nextInt();
        s.nextLine(); //moves to the next line so I don't have to bother casting
        String action = s.nextLine();
        while(!action.equals("---")) {
            actions = action.split(":");
            messages.put(actions[0], actions[1]);
            action = s.nextLine();
        }

    }

    public boolean goesBy(String name) {
        if(aliases.contains(name)){
            return true;
        }
        else{
            return false;
        }
    }

    public String getPrimaryName() {
        return primaryName;
    }
    
    public String getMessageForVerb(String verb) {
        if(messages.contains(verb)){
            return messages.get(verb);
        }
        else{
            return "you can't " + verb + " the " + primaryName;
        }
    }

    public String toString() {
        return primaryName;
    }

    public int getWeight() {
        return weight;
    }
}
